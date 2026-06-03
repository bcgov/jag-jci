package ca.bc.gov.open.jci;

import ca.bc.gov.open.jci.civil.GetCivilFileContent;
import ca.bc.gov.open.jci.common.code.values.GetCodeValues;
import ca.bc.gov.open.jci.common.criminal.file.content.GetCriminalFileContent;
import ca.bc.gov.open.jci.common.dev.utils.ClearAppearanceResults;
import ca.bc.gov.open.jci.common.dev.utils.RecreateCourtList;
import ca.bc.gov.open.jci.common.document.Document;
import ca.bc.gov.open.jci.common.document.GetDocument;
import ca.bc.gov.open.jci.common.process.results.*;
import ca.bc.gov.open.jci.common.rop.report.GetROPReport;
import ca.bc.gov.open.jci.common.user.login.GetUserLogin;
import ca.bc.gov.open.jci.common.user.mapping.GetParticipantInfo;
import ca.bc.gov.open.jci.common.user.mapping.MapGuidToParticipant;
import ca.bc.gov.open.jci.controllers.*;
import ca.bc.gov.open.jci.court.one.GetCrtList;
import ca.bc.gov.open.jci.exceptions.ORDSException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
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
    @Mock private HealthController healthController;
    @Mock private ProcessController processController;
    @Mock private CodeController codeController;
    @Mock private FileController fileController;
    @Mock private ReportController reportController;
    @Mock private UserController userController;
    @Mock private DevUtilsController devUtilsController;
    @Mock private DocumentController documentController;
    @Mock private CourtController courtController;
    @Mock private HttpServletRequest servletRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        healthController = Mockito.spy(new HealthController(restTemplate, objectMapper));
        processController = Mockito.spy(new ProcessController(restTemplate, objectMapper));
        codeController = Mockito.spy(new CodeController(restTemplate, objectMapper));
        fileController = Mockito.spy(new FileController(restTemplate, objectMapper));
        userController = Mockito.spy(new UserController(restTemplate, objectMapper));
        devUtilsController = Mockito.spy(new DevUtilsController(restTemplate, objectMapper));
        documentController = Mockito.spy(new DocumentController(restTemplate, objectMapper, servletRequest));
        courtController = Mockito.spy(new CourtController(restTemplate, objectMapper));
        reportController = Mockito.spy(new ReportController(restTemplate, objectMapper));
    }

    @Test
    public void testHealthOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> healthController.getHealth(new GetHealth()));
    }

    @Test
    public void testProcessVariationOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> processController.processVariation(new ProcessVariation()));
    }

    @Test
    public void testProcessSpeakerOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> processController.processSpeaker(new ProcessSpeaker()));
    }

    @Test
    public void testProcessCivilResultsOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> processController.processCivilResults(new ProcessCivilResults()));
    }

    @Test
    public void testProcessAppearanceMethodOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> processController.processAppearanceMethod(new ProcessAppearanceMethod()));
    }

    @Test
    public void testProcessPleaOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> processController.processPlea(new ProcessPlea()));
    }

    @Test
    public void testProcessElectionOrdsFail() {
         Assertions.assertThrows(
                ORDSException.class,
                () -> processController.processElection(new ProcessElection()));
    }

    @Test
    public void testProcessBailOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> processController.processBail(new ProcessBail()));
    }

    @Test
    public void testProcessCriminalResultOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> processController.processCriminalResult(new ProcessCriminalResult()));
    }

    @Test
    public void testProcessAgeNoticeOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> processController.processAgeNotice(new ProcessAgeNotice()));
    }

    @Test
    public void testProcessMatterCallOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> processController.processMatterCall(new ProcessMatterCall()));
    }

    @Test
    public void testProcessSentenceOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> processController.processSentence(new ProcessSentence()));
    }

    @Test
    public void testProcessBanOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> processController.processBan(new ProcessBan()));
    }

    @Test
    public void testProcessNoteOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> processController.processNote(new ProcessNote()));
    }

    @Test
    public void testProcessArraignmentOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> processController.processArraignment(new ProcessArraignment()));
    }

    @Test
    public void testProcessMoveOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> processController.processMove(new ProcessMove()));
    }

    @Test
    public void testProcessFindingOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> processController.processFinding(new ProcessFinding()));
    }

    @Test
    public void testProcessGenericResultOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> processController.processGenericResult(new ProcessGenericResult()));
    }

    @Test
    public void testProcessCivilAppearanceMethodOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () ->
                        processController.processCivilAppearanceMethod(
                                new ProcessCivilAppearanceMethod()));
    }

    @Test
    public void testProcessOrderOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> processController.processOrder(new ProcessOrder()));
    }

    @Test
    public void testProcessCivilOrderResultOrdsFail() {
          Assertions.assertThrows(
                ORDSException.class,
                () -> processController.processCivilOrderResult(new ProcessCivilOrderResult()));
    }

    @Test
    public void testProcessExhibitOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> processController.processExhibit(new ProcessExhibit()));
    }

    @Test
    public void testProcessSpecialCourtOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> processController.processSpecialCourt(new ProcessSpecialCourt()));
    }

    @Test
    public void testGetCodeValuesOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> codeController.getCodeValues(new GetCodeValues(), null));
    }

    @Test
    public void testGetCrtListOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> courtController.getCrtList(new GetCrtList()));
    }

    @Test
    public void testGetCriminalFileContentOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> fileController.getCriminalFileContent(new GetCriminalFileContent()));
    }

    @Test
    public void testGetCivilFileContentOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> fileController.getCivilFileContent(new GetCivilFileContent()));
    }

    @Test
    public void testGetROPReportOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> reportController.getRopReport(new GetROPReport()));
    }

    @Test
    public void testGetParticipantInfoOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> userController.getParticipantInfo(new GetParticipantInfo()));
    }

    @Test
    public void testGetNewParticipantInfoOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () ->
                        userController.getNewParticipantInfo(
                                new ca.bc.gov.open.jci.common.participant.info
                                        .GetParticipantInfo()));
    }

    @Test
    public void testMapGuidToParticipantOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> userController.mapGuidToParticipant(new MapGuidToParticipant()));
    }

    @Test
    public void testGetUserLoginOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class, () -> userController.getUserLogin(new GetUserLogin()));
    }

    @Test
    public void testGetClearAppearanceResultsOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> devUtilsController.clearAppearanceResults(new ClearAppearanceResults()));
    }

    @Test
    public void testGetRecreateCourtListOrdsFail() {
        Assertions.assertThrows(
                ORDSException.class,
                () -> devUtilsController.reCreateCourtList(new RecreateCourtList()));
    }

    @Test
    public void testGetDocumentOrdsFail() {
        GetDocument getDocument = new GetDocument();
        Document document = new Document();
        getDocument.setDocumentRequest(document);
        document.setDocumentId("123");
        Assertions.assertThrows(
                ORDSException.class, () -> documentController.getDocument(getDocument));
    }

    @Test
    public void securityTestFail_Then401() throws Exception {
        mockMvc.perform(post("/ws").contentType(MediaType.TEXT_XML))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }
}
