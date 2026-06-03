package ca.bc.gov.open.jci;

import ca.bc.gov.open.jci.common.document.secure.DocumentSecureRequest;
import ca.bc.gov.open.jci.common.document.secure.GetDocumentSecure;
import ca.bc.gov.open.jci.controllers.DocumentController;
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
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DocumentControllerTests {
    @Mock private ObjectMapper objectMapper;
    @Mock private RestTemplate restTemplate;
    @Mock private DocumentController documentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        documentController = Mockito.spy(new DocumentController(restTemplate, objectMapper));
    }

    @Test
    public void getDocumentSecureTest() throws JsonProcessingException {

        var req = new GetDocumentSecure();
        var one = new DocumentSecureRequest();
        req.setDocumentSecureRequest(one);

        Map<String, String> m = new HashMap<>();
        m.put(
                "url",
                "https://test:8443/courts/Servlet?&url=https%3A%2F%2Ftest.com%2Fpp%2Fdb%2Ftest%2Fget%2F%3FTicket%3DPcHWnmp");
        m.put("resultCd", "1");
        m.put("resultMessage", "success");
        m.put("status", "1");
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
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<byte[]>>any()))
                .thenReturn(responseEntity);

        var resp = documentController.getDocumentSecure(req);
        Assertions.assertNotNull(resp);
    }
}
