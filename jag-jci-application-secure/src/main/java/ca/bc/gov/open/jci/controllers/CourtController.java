package ca.bc.gov.open.jci.controllers;

import ca.bc.gov.open.jci.court.secure.one.GetCrtListSecure;
import ca.bc.gov.open.jci.court.secure.one.GetCrtListSecureResponse;
import ca.bc.gov.open.jci.exceptions.ORDSException;
import ca.bc.gov.open.jci.models.OrdsErrorLog;
import ca.bc.gov.open.jci.models.RequestSuccessLog;
import ca.bc.gov.open.jci.models.serializers.InstantSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Slf4j
@Endpoint
public class CourtController {
    @Value("${jci.host}")
    private String host = "https://127.0.0.1/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public CourtController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(
            namespace = "http://reeks.bcgov/CCD.Source.CourtLists.ws.provider:CourtListSecure",
            localPart = "getCrtListSecure")
    @ResponsePayload
    public GetCrtListSecureResponse getCrtListSecure(@RequestPayload GetCrtListSecure getCrtList)
            throws JsonProcessingException {

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "/common/courtlist/secure")
                        .queryParam(
                                "requestAgencyIdentifierId",
                                getCrtList.getRequestAgencyIdentifierId())
                        .queryParam("roomCd", getCrtList.getRoomCd())
                        .queryParam(
                                "proceedingDate",
                                InstantSerializer.convert(getCrtList.getProceedingDate()))
                        .queryParam("divisionCd", getCrtList.getDivisionCd())
                        .queryParam("fileNumber", getCrtList.getFileNumber())
                        .queryParam("agencyIdentifierCd", getCrtList.getAgencyIdentifierCd())
                        .queryParam("requestPartId", getCrtList.getRequestPartId())
                        .queryParam(
                                "requestDtm", InstantSerializer.convert(getCrtList.getRequestDtm()))
                        .queryParam("applicationCd", getCrtList.getApplicationCd());

        try {
            HttpEntity<GetCrtListSecureResponse> resp =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetCrtListSecureResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "getCrtListSecure")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getCrtListSecure",
                                    ex.getMessage(),
                                    getCrtList)));
            throw new ORDSException();
        }
    }
}
