package ca.bc.gov.open.jci.controllers;

import ca.bc.gov.open.jci.court.one.GetCrtList;
import ca.bc.gov.open.jci.court.one.GetCrtListResponse;
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
            namespace = "http://brooks/CCD.Source.CourtLists.ws.provider:CourtList",
            localPart = "getCrtList")
    @ResponsePayload
    public GetCrtListResponse getCrtList(@RequestPayload GetCrtList getCrtList)
            throws JsonProcessingException {

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "/common/courtlist")
                        .queryParam("agencyIdentifierCd", getCrtList.getAgencyIdentifierCd())
                        .queryParam("roomCd", getCrtList.getRoomCd())
                        .queryParam(
                                "proceedingDate",
                                InstantSerializer.convert(getCrtList.getProceedingDate()))
                        .queryParam("divisionCd", getCrtList.getDivisionCd())
                        .queryParam("fileNumber", getCrtList.getFileNumber());

        try {
            HttpEntity<GetCrtListResponse> resp =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetCrtListResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "getCrtList")));

            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getCrtList",
                                    ex.getMessage(),
                                    getCrtList)));
            throw new ORDSException();
        }
    }
}
