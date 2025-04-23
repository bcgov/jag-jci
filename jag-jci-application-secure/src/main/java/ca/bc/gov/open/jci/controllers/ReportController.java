package ca.bc.gov.open.jci.controllers;

import ca.bc.gov.open.jci.common.rop.report.secure.*;
import ca.bc.gov.open.jci.exceptions.ORDSException;
import ca.bc.gov.open.jci.models.OrdsErrorLog;
import ca.bc.gov.open.jci.models.RequestSuccessLog;
import ca.bc.gov.open.jci.models.serializers.InstantSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Endpoint
@Slf4j
public class ReportController {
    @Value("${jci.host}" + "criminal/")
    private String ordsHost = "https://127.0.0.1/";

    @Value("${jci.adobe-host}")
    private String adobeServerHost = "https://127.0.0.1/";

    @Value("${jci.report-app-name}")
    private String reportAppName = "";

    @Value("${jci.form-param-idx}")
    private String formParamIdx = "1";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ReportController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(
            namespace = "http://reeks.bcgov/CCD.Source.GetROPReport.ws:GetROPReportSecure",
            localPart = "getROPReportSecure")
    @ResponsePayload
    public GetROPReportSecureResponse getRopReportSecure(
            @RequestPayload GetROPReportSecure getROPReport) throws JsonProcessingException {

        var inner =
                getROPReport != null && getROPReport.getROPSecureRequest() != null
                        ? getROPReport.getROPSecureRequest()
                        : new RopSecureRequest();

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(ordsHost + "ropreport/secure")
                        .queryParam("requestAgencyId", inner.getRequestAgencyIdentifierId())
                        .queryParam("requestPartId", inner.getRequestPartId())
                        .queryParam("requestDtm", InstantSerializer.convert(inner.getRequestDtm()))
                        .queryParam("applicationCd", inner.getApplicationCd())
                        .queryParam("param1", inner.getParam1())
                        .queryParam("param2", inner.getParam2())
                        .queryParam("formCd", inner.getFormCd());

        HttpEntity<Map<String, String>> resp = null;
        try {

            // request url and key from ccd Report EndPoint
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
                                    "getROPReportSecure",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }

        if (resp == null || resp.getBody() == null) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getRopReportSecure",
                                    "Either response or its body is null while receiving the request getROPReportSecure's response.",
                                    inner)));
            throw new ORDSException();
        } else {
            // if got response from ORDS's getROPReportSecure response
            var out = new GetROPReportSecureResponse();
            var one = new ca.bc.gov.open.jci.common.rop.report.secure.RopResult();

            var body = resp.getBody();
            var respCd = body.get("responseCd");
            var respMsg = body.get("responseMessageTxt");
            var url = body.get("url");
            var keyValue = body.get("keyValue");

            // This is not shown in designer but the comparison tool is doing this
            one.setResultCd(respCd != null && respCd.equals("0") ? "2" : respCd);
            one.setResultMessage(respMsg);
            out.setROPResponse(one);

            if (url == null || url.isBlank()) {
                // return error
                log.error(
                        objectMapper.writeValueAsString(
                                new OrdsErrorLog(
                                        "Error received from ORDS",
                                        "getROPReport",
                                        "Null url fetched from ords",
                                        inner)));
                return out;
            }

            try {
                url = URLDecoder.decode(url, StandardCharsets.UTF_8);
                String query = "";
                if (url.contains("?")) {
                    query = url.split("\\?")[Integer.parseInt(formParamIdx)];
                }

                // build an adobe server uri using its url and parameters being return from ccd and
                // request base64 stream from this adobe server
                query =
                        query.replace(
                                        "<<FORM>>",
                                        inner.getFormCd() == null ? "" : inner.getFormCd())
                                .replace("<<APP>>", reportAppName)
                                .replace("<<TICKET>>", keyValue);

                String rpServerHost = url.length() > 0 ? adobeServerHost : url;
                String rpServerUri = rpServerHost + "?" + query;

                HttpEntity<byte[]> resp2 =
                        restTemplate.exchange(
                                rpServerUri,
                                HttpMethod.GET,
                                new HttpEntity<>(new HttpHeaders()),
                                byte[].class);

                String bs64 =
                        resp2.getBody() != null ? Base64.getEncoder().encodeToString(resp2.getBody()) : "";
                log.info(
                        objectMapper.writeValueAsString(
                                new RequestSuccessLog("Request Success", "getROPReportSecure")));
                one.setB64Content(bs64);
                return out;
            } catch (Exception ex) {
                log.error(
                        objectMapper.writeValueAsString(
                                new OrdsErrorLog(
                                        "Error received from ORDS",
                                        "getROPReportSecure",
                                        ex.getMessage(),
                                        inner)));
                throw new ORDSException();
            }
        }
    }
}
