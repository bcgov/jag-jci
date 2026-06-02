package ca.bc.gov.open.jci;

import ca.bc.gov.open.jci.common.process.results.GetHealth;
import ca.bc.gov.open.jci.common.process.results.GetHealthResponse;
import ca.bc.gov.open.jci.common.process.results.GetPing;
import ca.bc.gov.open.jci.common.process.results.GetPingResponse;
import ca.bc.gov.open.jci.controllers.HealthController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HealthControllerTests {

    @Mock private ObjectMapper objectMapper;
    @Mock private RestTemplate restTemplate;
    @Mock private HealthController healthController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        healthController = Mockito.spy(new HealthController(restTemplate, objectMapper));
    }

    @Test
    public void getPingTest() throws JsonProcessingException {

        var get = new GetPing();

        var out = new GetPingResponse();
        out.setStatus("A");

        ResponseEntity<GetPingResponse> responseEntity = new ResponseEntity<>(out, HttpStatus.OK);

        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetPingResponse>>any()))
                .thenReturn(responseEntity);

        var resp = healthController.getPing(get);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void getHealthTest() throws JsonProcessingException {

        var get = new GetHealth();

        var out = new GetHealthResponse();
        out.setAppid("A");
        out.setCompatibility("A");
        out.setHost("A");
        out.setStatus("A");
        out.setInstance("A");
        out.setVersion("A");
        out.setMethod("A");

        ResponseEntity<GetHealthResponse> responseEntity = new ResponseEntity<>(out, HttpStatus.OK);

        when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetHealthResponse>>any()))
                .thenReturn(responseEntity);

        var resp = healthController.getHealth(get);

        Assertions.assertNotNull(resp);
    }
}
