package ca.bc.gov.open.jci.controllers;

import ca.bc.gov.open.jci.civil.GetCivilFileContent;
import ca.bc.gov.open.jci.civil.GetCivilFileContentResponse;
import ca.bc.gov.open.jci.common.criminal.file.content.GetCriminalFileContent;
import ca.bc.gov.open.jci.common.criminal.file.content.GetCriminalFileContentResponse;
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
                    "http://courts.ag.gov.bc.ca/CCD.Source.CriminalFileContent.ws.provider:CriminalFileContent",
            localPart = "getCriminalFileContent")
    @ResponsePayload
    public GetCriminalFileContentResponse getCriminalFileContent(
            @RequestPayload GetCriminalFileContent getCriminalFileContent)
            throws JsonProcessingException {

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "criminal/" + "file")
                        .queryParam(
                                "agencyIdentifierCd",
                                getCriminalFileContent.getAgencyIdentifierCd())
                        .queryParam("roomCd", getCriminalFileContent.getRoomCd())
                        .queryParam(
                                "proceedingDate",
                                InstantSerializer.convert(
                                        getCriminalFileContent.getProceedingDate()))
                        .queryParam("appearanceId", getCriminalFileContent.getAppearanceID())
                        .queryParam("mdocJustinNo", getCriminalFileContent.getMdocJustinNo());

        try {
            HttpEntity<GetCriminalFileContentResponse> resp =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetCriminalFileContentResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "getCriminalFileContent")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getCriminalFileContent",
                                    ex.getMessage(),
                                    getCriminalFileContent)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(
            namespace =
                    "http://courts.ag.gov.bc.ca/CCD.Source.CivilFileContent.ws:CivilFileContent",
            localPart = "getCivilFileContent")
    @ResponsePayload
    public GetCivilFileContentResponse getCivilFileContent(
            @RequestPayload GetCivilFileContent getCivilFileContent)
            throws JsonProcessingException {

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "civil/" + "file")
                        .queryParam("courtLocaCd", getCivilFileContent.getCourtLocaCd())
                        .queryParam("courtRoomCd", getCivilFileContent.getCourtRoomCd())
                        .queryParam(
                                "courtProceedingDate",
                                InstantSerializer.convert(
                                        getCivilFileContent.getCourtProceedingDate()))
                        .queryParam("appearanceId", getCivilFileContent.getAppearanceId())
                        .queryParam("physicalFileId", getCivilFileContent.getPhysicalFileId())
                        .queryParam("applicationCd", getCivilFileContent.getApplicationCd());

        try {
            HttpEntity<GetCivilFileContentResponse> resp =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetCivilFileContentResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "getCivilFileContent")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getCivilFileContent",
                                    ex.getMessage(),
                                    getCivilFileContent)));
            throw new ORDSException();
        }
    }
}
