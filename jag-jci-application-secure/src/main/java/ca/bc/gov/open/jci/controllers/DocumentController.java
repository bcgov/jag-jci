package ca.bc.gov.open.jci.controllers;

import ca.bc.gov.open.jci.common.document.secure.DocumentSecureRequest;
import ca.bc.gov.open.jci.common.document.secure.GetDocumentSecure;
import ca.bc.gov.open.jci.common.document.secure.GetDocumentSecureResponse;
import ca.bc.gov.open.jci.exceptions.ORDSException;
import ca.bc.gov.open.jci.models.OrdsErrorLog;
import ca.bc.gov.open.jci.models.RequestSuccessLog;
import ca.bc.gov.open.jci.models.serializers.InstantSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
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

    @Autowired
    public DocumentController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(
            namespace =
                    "http://reeks.bcgov/CCD.Source.GetDocument.ws:CCD.Source.GetDocument.ws:GetDocumentSecure",
            localPart = "getDocumentSecure")
    @ResponsePayload
    public GetDocumentSecureResponse getDocumentSecure(@RequestPayload GetDocumentSecure document)
            throws JsonProcessingException {

        var inner =
                document.getDocumentSecureRequest() != null
                        ? document.getDocumentSecureRequest()
                        : new DocumentSecureRequest();

        // request getDocument to get url
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "common/document/secure")
                        .queryParam(
                                "requestAgencyIdentifierId", inner.getRequestAgencyIdentifierId())
                        .queryParam("requestPartId", inner.getRequestPartId())
                        .queryParam("requestDtm", InstantSerializer.convert(inner.getRequestDtm()))
                        .queryParam("applicationCd", inner.getApplicationCd())
                        .queryParam("documentId", inner.getDocumentId())
                        .queryParam("courtDivisionCd", inner.getCourtDivisionCd())
                        .queryParam("physicalFileId", inner.getPhysicalFileId())
                        .queryParam("mdocJustinNo", inner.getMdocJustinNo());

        HttpEntity<Map<String, String>> resp = null;
        try {
            resp =
                    restTemplate.exchange(
                            builder.build().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            new ParameterizedTypeReference<>() {});
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getDocumentSecure",
                                    ex.getMessage(),
                                    document)));
            throw new ORDSException();
        }

        if (resp != null && resp.getBody() != null) {
            var body = resp.getBody();
            String resultCd = body.get("resultCd");
            String resultMessage = body.get("resultMessage");
            String url = body.get("url");

            var out = new GetDocumentSecureResponse();
            var one = new ca.bc.gov.open.jci.common.document.secure.DocumentResult();

            out.setDocumentResponse(one);
            one.setResultCd(resultCd);
            one.setResultMessage(resultMessage);

            if (url == null) {
                // process the response's error messages which are return from the ORDS getDocument
                // API
                log.info(
                        objectMapper.writeValueAsString(
                                new RequestSuccessLog("Request Success", "getDocumentSecure")));
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
                                new HttpEntity<>(new HttpHeaders()),
                                byte[].class);

                String bs64 =
                        resp2.getBody() != null ? Base64.getEncoder().encodeToString(resp2.getBody()) : "";

                one.setB64Content(bs64);
                out.setDocumentResponse(one);
                log.info(
                        objectMapper.writeValueAsString(
                                new RequestSuccessLog("Request Success", "getDocumentSecure")));
                return out;
            } catch (Exception ex) {
                log.error(
                        objectMapper.writeValueAsString(
                                new OrdsErrorLog(
                                        "Error occurred while getting base64 document",
                                        "getDocumentSecure",
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
                                "getDocumentSecure",
                                "Either response or its body is null while receiving the request getDocumentSecure's response.",
                                inner)));
        throw new ORDSException();
    }
}
