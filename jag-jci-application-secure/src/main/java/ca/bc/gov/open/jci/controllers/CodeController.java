package ca.bc.gov.open.jci.controllers;

import ca.bc.gov.open.jci.common.code.values.secure.GetCodeValuesSecure;
import ca.bc.gov.open.jci.common.code.values.secure.GetCodeValuesSecureResponse;
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
public class CodeController {
    @Value("${jci.host}" + "common/")
    private String host = "https://127.0.0.1/";

    @Value("${jci.generic-agen-id}")
    private String genericAgenId;

    @Value("${jci.generic-part-id}")
    private String genericPartId;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public CodeController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(
            namespace = "http://reeks.bcgov/CCD.Source.CodeValues.ws.provider:CodeValuesSecure",
            localPart = "getCodeValuesSecure")
    @ResponsePayload
    public GetCodeValuesSecureResponse getCodeValuesSecure(
            @RequestPayload GetCodeValuesSecure getCodeValues) throws JsonProcessingException {

        String secureExtension =
                getCodeValues.getRequestAgencyIdentifierId() != null
                                && getCodeValues.getRequestPartId() != null
                                && getCodeValues
                                        .getRequestAgencyIdentifierId()
                                        .equals(genericAgenId)
                                && getCodeValues.getRequestPartId().equals(genericPartId)
                        ? ""
                        : "/secure";

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "codevalues" + secureExtension)
                        .queryParam("requestAgencyId", getCodeValues.getRequestAgencyIdentifierId())
                        .queryParam("requestPartId", getCodeValues.getRequestPartId())
                        .queryParam(
                                "lastRetrievedDate",
                                InstantSerializer.convert(getCodeValues.getLastRetrievedDate()))
                        .queryParam(
                                "requestDtm",
                                InstantSerializer.convert(getCodeValues.getRequestDtm()))
                        .queryParam("applicationCd", getCodeValues.getApplicationCd());

        try {
            HttpEntity<GetCodeValuesSecureResponse> resp =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetCodeValuesSecureResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "getCodeValuesSecure")));
            if (resp.getBody() != null && resp.getBody().getResultCd() == null
                    || resp.getBody().getResultCd().isEmpty()) {
                resp.getBody().setResultCd("0");
            }

            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getCodeValuesSecure",
                                    ex.getMessage(),
                                    getCodeValues)));
            throw new ORDSException();
        }
    }
}
