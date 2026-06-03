package ca.bc.gov.open.jci;

import ca.bc.gov.open.jci.civil.secure.GetCivilFileContentSecure;
import ca.bc.gov.open.jci.common.code.values.secure.GetCodeValuesSecure;
import ca.bc.gov.open.jci.common.criminal.file.content.secure.GetCriminalFileContentSecure;
import ca.bc.gov.open.jci.common.document.secure.GetDocumentSecure;
import ca.bc.gov.open.jci.common.rop.report.secure.GetROPReportSecure;
import ca.bc.gov.open.jci.controllers.*;
import ca.bc.gov.open.jci.court.secure.one.GetCrtListSecure;
import ca.bc.gov.open.jci.exceptions.ORDSException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrdsErrorTests {
    @Autowired private MockMvc mockMvc;

    @Mock private ObjectMapper objectMapper;
    @Mock private RestTemplate restTemplate;
    @Mock private CodeController codeController;
    @Mock private CourtController courtController;
    @Mock private FileController fileController;
    @Mock private ReportController reportController;
    @Mock private DocumentController documentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        codeController = Mockito.spy(new CodeController(restTemplate, objectMapper));
        courtController = Mockito.spy(new CourtController(restTemplate, objectMapper));
        fileController = Mockito.spy(new FileController(restTemplate, objectMapper));
        reportController = Mockito.spy(new ReportController(restTemplate, objectMapper));
        documentController = Mockito.spy(new DocumentController(restTemplate, objectMapper));

    }

    @Test
    public void testGetCodeValuesSecureOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> codeController.getCodeValuesSecure(new GetCodeValuesSecure()));
    }

    @Test
    public void testGetCrtListSecureOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> courtController.getCrtListSecure(new GetCrtListSecure()));
    }

    @Test
    public void testGetCriminalFileContentSecureOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () ->
                        fileController.getCriminalFileContentSecure(
                                new GetCriminalFileContentSecure()));
    }

    @Test
    public void testGetCivilFileContentSecureOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> fileController.getCivilFileContentSecure(new GetCivilFileContentSecure()));
    }

    @Test
    public void testGetROPReportSecureOrdsFail() {

        Assertions.assertThrows(
                ORDSException.class,
                () -> reportController.getRopReportSecure(new GetROPReportSecure()));
    }

    @Test
    public void testGetDocumentSecureOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> documentController.getDocumentSecure(new GetDocumentSecure()));
    }

    @Test
    public void securityTestFail_Then401() throws Exception {
        mockMvc.perform(post("/ws").contentType(MediaType.TEXT_XML))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }
}
