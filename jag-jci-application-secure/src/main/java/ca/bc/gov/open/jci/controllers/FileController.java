package ca.bc.gov.open.jci.controllers;

import ca.bc.gov.open.jci.civil.secure.GetCivilFileContentSecure;
import ca.bc.gov.open.jci.civil.secure.GetCivilFileContentSecureResponse;
import ca.bc.gov.open.jci.common.criminal.file.content.secure.GetCriminalFileContentSecure;
import ca.bc.gov.open.jci.common.criminal.file.content.secure.GetCriminalFileContentSecureResponse;
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
public class FileController {

    @Value("${jci.host}")
    private String host = "https://127.0.0.1/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public FileController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(
            namespace =
                    "http://reeks.bcgov/CCD.Source.CriminalFileContent.ws.provider:CriminalFileContentSecure",
            localPart = "getCriminalFileContentSecure")
    @ResponsePayload
    public GetCriminalFileContentSecureResponse getCriminalFileContentSecure(
            @RequestPayload GetCriminalFileContentSecure getCriminalFileContent)
            throws JsonProcessingException {

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "criminal/" + "file/secure")
                        .queryParam(
                                "agencyIdentifierCd",
                                getCriminalFileContent.getAgencyIdentifierCd())
                        .queryParam("roomCd", getCriminalFileContent.getRoomCd())
                        .queryParam(
                                "proceedingDate",
                                InstantSerializer.convert(
                                        getCriminalFileContent.getProceedingDate()))
                        .queryParam("appearanceId", getCriminalFileContent.getAppearanceID())
                        .queryParam("mdocJustinNo", getCriminalFileContent.getMdocJustinNo())
                        .queryParam(
                                "requestAgencyId",
                                getCriminalFileContent.getRequestAgencyIdentifierId())
                        .queryParam("requestPartId", getCriminalFileContent.getRequestPartId())
                        .queryParam(
                                "requestDtm",
                                InstantSerializer.convert(getCriminalFileContent.getRequestDtm()))
                        .queryParam("applicationCd", getCriminalFileContent.getApplicationCd());

        try {
            HttpEntity<GetCriminalFileContentSecureResponse> resp =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetCriminalFileContentSecureResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "getCriminalFileContentSecure")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getCriminalFileContentSecure",
                                    ex.getMessage(),
                                    getCriminalFileContent)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(
            namespace =
                    "http://courts.ag.gov.bc.ca/CCD.Source.CivilFileContent.ws:CivilFileContent",
            localPart = "getCivilFileContentSecure")
    @ResponsePayload
    public GetCivilFileContentSecureResponse getCivilFileContentSecure(
            @RequestPayload GetCivilFileContentSecure getCivilFileContent)
            throws JsonProcessingException {

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "civil/" + "file/secure")
                        .queryParam("courtLocaCd", getCivilFileContent.getCourtLocaCd())
                        .queryParam("courtRoomCd", getCivilFileContent.getCourtRoomCd())
                        .queryParam(
                                "courtProceedingDate",
                                InstantSerializer.convert(
                                        getCivilFileContent.getCourtProceedingDate()))
                        .queryParam("appearanceId", getCivilFileContent.getAppearanceId())
                        .queryParam("physicalFileId", getCivilFileContent.getPhysicalFileId())
                        .queryParam(
                                "requestAgencyId",
                                getCivilFileContent.getRequestAgencyIdentifierId())
                        .queryParam("requestPartId", getCivilFileContent.getRequestPartId())
                        .queryParam(
                                "requestDtm",
                                InstantSerializer.convert(getCivilFileContent.getRequestDtm()))
                        .queryParam("applicationCd", getCivilFileContent.getApplicationCd());

        try {
            HttpEntity<ca.bc.gov.open.jci.civil.secure.GetCivilFileContentSecureResponse> resp =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            ca.bc.gov.open.jci.civil.secure.GetCivilFileContentSecureResponse
                                    .class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "getCivilFileContentSecure")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getCivilFileContentSecure",
                                    ex.getMessage(),
                                    getCivilFileContent)));
            throw new ORDSException();
        }
    }
}
