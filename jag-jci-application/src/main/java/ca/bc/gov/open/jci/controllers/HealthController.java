package ca.bc.gov.open.jci.controllers;

import ca.bc.gov.open.jci.common.process.results.GetHealth;
import ca.bc.gov.open.jci.common.process.results.GetHealthResponse;
import ca.bc.gov.open.jci.common.process.results.GetPing;
import ca.bc.gov.open.jci.common.process.results.GetPingResponse;
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
public class HealthController {

    @Value("${jci.host}" + "common/")
    private String host = "https://127.0.0.1/";

    private static final String PROCESS_NAMESPACE =
            "http://court.ag.gov.bc.ca/CCD.Source.ProcessResults.ws.provider:ProcessResults";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public HealthController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "getHealth")
    @ResponsePayload
    public GetHealthResponse getHealth(@RequestPayload GetHealth empty)
            throws JsonProcessingException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "health");

        try {
            HttpEntity<GetHealthResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetHealthResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "getHealth")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getHealth",
                                    ex.getMessage(),
                                    empty)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = PROCESS_NAMESPACE, localPart = "getPing")
    @ResponsePayload
    public GetPingResponse getPing(@RequestPayload GetPing empty) throws JsonProcessingException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "ping");
        try {
            HttpEntity<GetPingResponse> resp =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetPingResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "getPing")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getPing",
                                    ex.getMessage(),
                                    empty)));
            throw new ORDSException();
        }
    }
}
