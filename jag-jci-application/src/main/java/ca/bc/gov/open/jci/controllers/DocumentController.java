package ca.bc.gov.open.jci.controllers;

import ca.bc.gov.open.jci.common.document.Document;
import ca.bc.gov.open.jci.common.document.DocumentResult;
import ca.bc.gov.open.jci.common.document.GetDocument;
import ca.bc.gov.open.jci.common.document.GetDocumentResponse;
import ca.bc.gov.open.jci.exceptions.ORDSException;
import ca.bc.gov.open.jci.models.OrdsErrorLog;
import ca.bc.gov.open.jci.models.RequestSuccessLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import java.util.Base64;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Slf4j
@Endpoint
public class DocumentController {

    @Value("${jci.host}")
    private String host = "https://127.0.0.1/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final HttpServletRequest servletRequest;

    private static String CORRELATION_HEADER_NAME = "x-correlation-id";

    @Autowired
    public DocumentController(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            HttpServletRequest servletRequest) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.servletRequest = servletRequest;
    }

    @PayloadRoot(
            namespace = "http://courts.gov.bc.ca/CCD.Source.GetDocument.ws:GetDocument",
            localPart = "getDocument")
    @ResponsePayload
    public GetDocumentResponse getDocument(@RequestPayload GetDocument document)
            throws JsonProcessingException {

        var inner =
                document.getDocumentRequest() != null
                        ? document.getDocumentRequest()
                        : new Document();

        // request getDocument to get url
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "common/document")
                        .queryParam(
                                "documentId",
                                URLEncoder.encode(inner.getDocumentId(), StandardCharsets.UTF_8))
                        .queryParam("courtDivisionCd", inner.getCourtDivisionCd());

        HttpEntity<Map<String, String>> resp = null;
        LocalDateTime startTime = LocalDateTime.now();
        HttpHeaders headers = new HttpHeaders();
        if (servletRequest.getHeader(CORRELATION_HEADER_NAME) != null) {
            headers.set(CORRELATION_HEADER_NAME, servletRequest.getHeader(CORRELATION_HEADER_NAME));
        }
        try {
            resp =
                    restTemplate.exchange(
                            builder.build(true).toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            new ParameterizedTypeReference<>() {});
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getDocument",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }

        if (resp != null && resp.getBody() != null) {
            var body = resp.getBody();
            String resultCd = body.get("resultCd");
            String resultMessage = body.get("resultMessage");
            String url = body.get("url");
            if (url == null) {
                // process the response's error messages which are return from the ORDS getDocument
                // API
                log.info(
                        objectMapper.writeValueAsString(
                                new RequestSuccessLog("Request Success", "getDocument")));

                var out = new GetDocumentResponse();
                var one = new DocumentResult();
                out.setDocumentResponse(one);
                one.setResultCd(resultCd);
                one.setResultMessage(resultMessage);
                return out;
            }

            // request uri to get base64 document

            try {
                // get the ticket
                url = URLDecoder.decode(url, StandardCharsets.UTF_8);

                HttpEntity<byte[]> resp2 =
                        restTemplate.exchange(
                                new URI(url),
                                HttpMethod.GET,
                                new HttpEntity<>(headers),
                                byte[].class);

                String bs64 =
                        resp2.getBody() != null ? Base64.getEncoder().encodeToString(resp2.getBody()) : "";

                var out = new GetDocumentResponse();
                var one = new DocumentResult();
                one.setB64Content(bs64);
                out.setDocumentResponse(one);
                one.setResultCd(resultCd);
                one.setResultMessage(resultMessage);
                log.info(
                        objectMapper.writeValueAsString(
                                new RequestSuccessLog("Request Success", "getDocument")));
                LogGetDocumentPerformance(
                        startTime, servletRequest.getHeader(CORRELATION_HEADER_NAME));
                return out;
            } catch (Exception ex) {
                log.error(
                        objectMapper.writeValueAsString(
                                new OrdsErrorLog(
                                        "Error occurred while getting base64 document",
                                        "getDocument",
                                        ex.getMessage(),
                                        inner)));
                throw new ORDSException();
            }
        }

        // the leftover is not invalid scenarios such as, a null response body.
        log.error(
                objectMapper.writeValueAsString(
                        new OrdsErrorLog(
                                "Error received from ORDS",
                                "getDocument",
                                "Either response or its body is null while receiving the request getDocument's response.",
                                inner)));
        throw new ORDSException();
    }

    private static void LogGetDocumentPerformance(LocalDateTime start, String correlationId) {
        Duration duration = Duration.between(start, LocalDateTime.now());
        log.info(
                "GetDocument Performance - Duration:"
                        + duration.toMillis() / 1000.0
                        + " CorrelationId:"
                        + correlationId);
    }
}
