package ca.bc.gov.open.jci.controllers;

import ca.bc.gov.open.jci.common.user.login.GetUserLogin;
import ca.bc.gov.open.jci.common.user.login.GetUserLoginRequestType;
import ca.bc.gov.open.jci.common.user.login.GetUserLoginResponse;
import ca.bc.gov.open.jci.common.user.login.GetUserLoginResponseType;
import ca.bc.gov.open.jci.common.user.mapping.GetParticipantInfo;
import ca.bc.gov.open.jci.common.user.mapping.GetParticipantInfoResponse;
import ca.bc.gov.open.jci.common.user.mapping.MapGuidToParticipant;
import ca.bc.gov.open.jci.common.user.mapping.MapGuidToParticipantResponse;
import ca.bc.gov.open.jci.exceptions.ORDSException;
import ca.bc.gov.open.jci.models.OrdsErrorLog;
import ca.bc.gov.open.jci.models.RequestSuccessLog;
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

@Endpoint
@Slf4j
public class UserController {
    @Value("${jci.host}" + "common/")
    private String host = "https://127.0.0.1/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(
            namespace = "http://courts.ag.gov.bc.ca/CCD.Source.CCDUserMapping.ws:ccdUserMapping",
            localPart = "getParticipantInfo")
    @ResponsePayload
    public GetParticipantInfoResponse getParticipantInfo(
            @RequestPayload GetParticipantInfo getParticipantInfo) throws JsonProcessingException {
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "user/mapping/participant-info")
                        .queryParam("guid", getParticipantInfo.getGuid());

        try {
            HttpEntity<GetParticipantInfoResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetParticipantInfoResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "getParticipantInfo")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getParticipantInfo",
                                    ex.getMessage(),
                                    getParticipantInfo)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(
            namespace = "http://reeks.bcgov/CCD.Source.GetParticipantInfo.WS:getParticipantInfo",
            localPart = "getParticipantInfo")
    @ResponsePayload
    public ca.bc.gov.open.jci.common.participant.info.GetParticipantInfoResponse
            getNewParticipantInfo(
                    @RequestPayload
                            ca.bc.gov.open.jci.common.participant.info.GetParticipantInfo
                                    getParticipantInfo)
                    throws JsonProcessingException {
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "participant-info")
                        .queryParam("guid", getParticipantInfo.getGUID());

        try {
            HttpEntity<ca.bc.gov.open.jci.common.participant.info.GetParticipantInfoResponseEx>
                    resp =
                            restTemplate.exchange(
                                    builder.toUriString(),
                                    HttpMethod.GET,
                                    new HttpEntity<>(new HttpHeaders()),
                                    ca.bc.gov.open.jci.common.participant.info
                                            .GetParticipantInfoResponseEx.class);
            ca.bc.gov.open.jci.common.participant.info.GetParticipantInfoResponse out =
                    new ca.bc.gov.open.jci.common.participant.info.GetParticipantInfoResponse();
            out.setGetParticipantInfoResponse(resp.getBody());
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "new getParticipantInfo")));
            return out;
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getParticipantInfo",
                                    ex.getMessage(),
                                    getParticipantInfo)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(
            namespace = "http://courts.ag.gov.bc.ca/CCD.Source.CCDUserMapping.ws:ccdUserMapping",
            localPart = "mapGuidToParticipant")
    @ResponsePayload
    public MapGuidToParticipantResponse mapGuidToParticipant(
            @RequestPayload MapGuidToParticipant mapGuidToParticipant)
            throws JsonProcessingException {
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "user/mapping/partid-to-guid")
                        .queryParam("guid", mapGuidToParticipant.getGuid())
                        .queryParam("partId", mapGuidToParticipant.getPartId())
                        .queryParam("idirId", mapGuidToParticipant.getIdirId());

        try {
            HttpEntity<MapGuidToParticipantResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.POST,
                            new HttpEntity<>(new HttpHeaders()),
                            MapGuidToParticipantResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "mapGuidToParticipant")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "mapGuidToParticipant",
                                    ex.getMessage(),
                                    mapGuidToParticipant)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(
            namespace = "http://brooks/CCD.Source.GetUserLogin.WS:getUserLogin",
            localPart = "GetUserLogin")
    @ResponsePayload
    public GetUserLoginResponse getUserLogin(@RequestPayload GetUserLogin getUserLogin)
            throws JsonProcessingException {
        var inner =
                getUserLogin.getRequest() != null
                        ? getUserLogin.getRequest()
                        : new GetUserLoginRequestType();

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "user/login")
                        .queryParam("temporaryAccessGuid", inner.getTemporaryAccessGuid())
                        .queryParam("domainUserGuid", inner.getDomainUserGuid())
                        .queryParam("domainUserId", inner.getDomainUserId());

        try {
            HttpEntity<GetUserLoginResponseType> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetUserLoginResponseType.class);
            var out = new GetUserLoginResponse();
            out.setResponse(resp.getBody());
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "getUserLogin")));
            return out;
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getUserLogin",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }
}
