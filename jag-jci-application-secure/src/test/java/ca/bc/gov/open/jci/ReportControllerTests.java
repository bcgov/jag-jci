package ca.bc.gov.open.jci;

import ca.bc.gov.open.jci.common.rop.report.secure.GetROPReportSecure;
import ca.bc.gov.open.jci.common.rop.report.secure.RopSecureRequest;
import ca.bc.gov.open.jci.controllers.ReportController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReportControllerTests {

    @Mock private ObjectMapper objectMapper;
    @Mock private RestTemplate restTemplate;
    @Mock private ReportController reportController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reportController = Mockito.spy(new ReportController(restTemplate, objectMapper));
    }

    @Test
    public void getRopReportSecureTest() throws JsonProcessingException {

        var req = new GetROPReportSecure();
        var one = new RopSecureRequest();

        one.setParam1("A");
        one.setParam2("A");
        one.setFormCd("A");
        one.setRequestAgencyIdentifierId("A");
        one.setRequestPartId("A");
        one.setRequestDtm(Instant.now());
        one.setApplicationCd("A");

        req.setROPSecureRequest(one);

        Map<String, String> m = new HashMap<>();
        m.put("url", "http://127.0.0.1?param1=<<FORM>>&&param3=<<TICKET>>");
        m.put("keyValue", "tokenvalue");
        ResponseEntity<Map<String, String>> responseEntity2 =
                new ResponseEntity<>(m, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<ParameterizedTypeReference<Map<String, String>>>any()))
                .thenReturn(responseEntity2);

        var out = "A";
        ResponseEntity<byte[]> responseEntity =
                new ResponseEntity<>(out.getBytes(StandardCharsets.UTF_8), HttpStatus.OK);

        // Set up to adobe response
        when(restTemplate.exchange(
                        Mockito.matches("tokenvalue"),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<byte[]>>any()))
                .thenReturn(responseEntity);

        var resp = reportController.getRopReportSecure(req);

        Assertions.assertNotNull(resp);
    }
}
