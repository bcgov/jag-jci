package ca.bc.gov.open.jci;

import ca.bc.gov.open.jci.civil.secure.GetCivilFileContentSecure;
import ca.bc.gov.open.jci.civil.secure.GetCivilFileContentSecureResponse;
import ca.bc.gov.open.jci.common.criminal.file.content.AppearanceCountTypes;
import ca.bc.gov.open.jci.common.criminal.file.content.AppearanceTypes;
import ca.bc.gov.open.jci.common.criminal.file.content.PartyAppearanceMethodType;
import ca.bc.gov.open.jci.common.criminal.file.content.SentenceType;
import ca.bc.gov.open.jci.common.criminal.file.content.secure.GetCriminalFileContentSecure;
import ca.bc.gov.open.jci.common.criminal.file.content.secure.GetCriminalFileContentSecureResponse;
import ca.bc.gov.open.jci.controllers.FileController;
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

import java.net.URI;
import java.time.Instant;

import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileControllerTests {
    @Mock private ObjectMapper objectMapper;
    @Mock private RestTemplate restTemplate;
    @Mock private FileController fileController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fileController = Mockito.spy(new FileController(restTemplate, objectMapper));
    }

    @Test
    public void getCriminalFileContentSecureTest() throws JsonProcessingException {
        var req = new GetCriminalFileContentSecure();
        req.setRequestAgencyIdentifierId("A");
        req.setRequestPartId("A");
        req.setRequestDtm(Instant.now());
        req.setApplicationCd("A");
        req.setAgencyIdentifierCd("A");
        req.setRoomCd("A");
        req.setProceedingDate(Instant.now());
        req.setAppearanceID("A");
        req.setMdocJustinNo("A");

        var out = new GetCriminalFileContentSecureResponse();
        out.setResultCd("A");
        out.setResultMessage("A");
        var fc = new ca.bc.gov.open.jci.common.criminal.file.content.secure.FileContentDoc();

        fc.setCourtLocaCd("A");
        fc.setCourtRoomCd("A");
        fc.setCourtProceedingDate(Instant.now());
        fc.getAppearanceId().add("A");
        fc.setMdocJustinNo("A");
        var ac = new ca.bc.gov.open.jci.common.criminal.file.content.secure.AccusedFileType();

        var ap = new ca.bc.gov.open.jci.common.criminal.file.content.secure.AppearanceTypes();
        ap.setAppearanceId("A");
        ap.setAppearanceDate(Instant.now());
        ap.setCourtAgencyIdentifier("A");
        ap.setCourtRoom("A");
        ap.setAppearanceNote("A");
        ap.setEstimatedTimeHour("A");
        ap.setEstimatedTimeMin("A");
        ap.setConfirmStatusDsc("A");
        ap.setJudgesRecommendation("A");

        var act = new ca.bc.gov.open.jci.common.criminal.file.content.secure.AppearanceCountTypes();
        act.setAppcId("A");
        act.setCountNumber("A");
        act.setAppearanceReason("A");
        act.setAppearanceResult("A");
        act.setFinding("A");
        act.setSectionDscTxt("A");
        act.setSectionTxt("A");
        var st = new ca.bc.gov.open.jci.common.criminal.file.content.secure.SentenceType();

        st.setSntpCd("A");
        st.setSentTermPeriodQty("A");
        st.setSentTermCd("A");
        st.setSentSubtermPeriodQty("A");
        st.setSentSubtermCd("A");
        st.setSentTertiaryTermPeriodQty("A");
        st.setSentTertiaryTermCd("A");
        st.setSentIntermittentYn("A");
        st.setSentMonetaryAmt("A");
        st.setSentDueTtpDt(Instant.now());
        st.setSentEffectiveDt(Instant.now());
        st.setSentDetailTxt("A");
        st.setSentYcjaAdultYouthCd("A");
        st.setSentCustodySecureYn("A");

        act.getSentence().add(st);

        ap.getAppearanceCount().add(act);

        ac.getAppearance().add(ap);

        ac.setFileNumber("A");
        ac.setFileLocaAgencyIdentifierCd("A");
        ac.setMdocJustinNo("A");
        ac.setMDocInfoSeqNo("A");
        ac.setPhysTicketSeriesTxt("A");
        ac.setPhyFileFolderNo("A");
        ac.setCourtClassCd("A");
        ac.setCourtLevelCd("A");
        ac.setPartId("A");
        ac.setProfSeqNo("A");
        ac.setSealTypeCd("A");
        ac.setFiledDate(Instant.now());

        var wt = new ca.bc.gov.open.jci.common.criminal.file.content.secure.ArrestWarrantType();
        wt.setFileNumberText("A");
        wt.setWarrantDate(Instant.now());
        ac.getArrestWarrant().add(wt);

        var bt = new ca.bc.gov.open.jci.common.criminal.file.content.secure.BanTypes();
        bt.setBanTypeCd("A");
        bt.setBanTypeDescription("A");
        bt.setBanTypeAct("A");
        bt.setBanTypeSection("A");
        bt.setBanTypeSubSection("A");
        bt.setBanStatuteId("A");
        bt.setBanCommentText("A");
        bt.setBanOrderedDate(Instant.now());
        bt.setBanSeqNo("A");
        ac.getBan().add(bt);

        var pt = new ca.bc.gov.open.jci.common.criminal.file.content.secure.ProtectionOrderType();
        pt.setPOROrderIssueDate(Instant.now());
        pt.setOrderTypeDsc("A");
        pt.setPORConditionText("A");
        ac.getProtectionOrder().add(pt);

        var ct = new ca.bc.gov.open.jci.common.criminal.file.content.secure.CFCOrderType();
        ct.setCFCOrderIssueDate(Instant.now());
        ct.setCFCConditionText("A");
        ct.setOrderTypeDsc("A");
        ac.getCFCOrder().add(ct);

        var ht =
                new ca.bc.gov.open.jci.common.criminal.file.content.secure.HearingRestrictionType();
        ht.setHearingRestrictiontype("A");
        ht.setJudgeName("A");
        ht.setHearingRestrictionDate(Instant.now());
        ac.getHearingRestriction().add(ht);

        var dt = new ca.bc.gov.open.jci.common.criminal.file.content.secure.DocumentType();
        dt.setDocmClassification("A");
        dt.setDocmId("A");
        dt.setIssueDate(Instant.now());
        dt.setDocmFormId("A");
        dt.setDocmFormDsc("A");
        dt.setDocmDispositionDsc("A");
        dt.setDocmDispositionDate(Instant.now());
        dt.setImageId("A");
        dt.setDocumentPageCount("A");
        ac.getDocument().add(dt);

        AppearanceTypes at = new AppearanceTypes();
        at.setAppearanceId("A");
        at.setAppearanceDate(Instant.now());
        at.setCourtAgencyIdentifier("A");
        at.setCourtRoom("A");
        at.setAppearanceNote("A");
        at.setEstimatedTimeHour("A");
        at.setEstimatedTimeMin("A");
        at.setConfirmStatusDsc("A");
        at.setJudgesRecommendation("A");

        var pa =
                new ca.bc.gov.open.jci.common.criminal.file.content.secure
                        .PartyAppearanceMethodType();

        pa.setPartyName("A");
        pa.setPartyAppearanceMethod("A");
        pa.setPartyRole("A");
        pa.setPartId("A");

        AppearanceCountTypes apc = new AppearanceCountTypes();
        apc.setAppcId("A");
        apc.setCountNumber("A");
        apc.setAppearanceReason("A");
        apc.setAppearanceResult("A");
        apc.setFinding("A");
        apc.setSectionTxt("A");
        apc.setSectionDscTxt("A");
        SentenceType st2 = new SentenceType();
        st2.setSntpCd("A");
        st2.setSentTermPeriodQty("A");
        st2.setSentTermCd("A");
        st2.setSentSubtermPeriodQty("A");
        st2.setSentSubtermCd("A");
        st2.setSentTertiaryTermCd("A");
        st2.setSentIntermittentYn("A");
        st2.setSentMonetaryAmt("A");
        st2.setSentDueTtpDt(Instant.now());
        st2.setSentEffectiveDt(Instant.now());
        st2.setSentDetailTxt("A");
        st2.setSentYcjaAdultYouthCd("A");
        st2.setSentCustodySecureYn("A");
        apc.getSentence().add(st2);
        at.getAppearanceCount().add(apc);

        PartyAppearanceMethodType pmt = new PartyAppearanceMethodType();
        pmt.setPartyName("A");
        pmt.setPartyRole("A");
        pmt.setPartId("A");
        pmt.setPartyAppearanceMethod("A");
        at.getPartyAppearanceMethod().add(pmt);

        out.setFileContent(fc);

        ResponseEntity<GetCriminalFileContentSecureResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetCriminalFileContentSecureResponse>>any()))
                .thenReturn(responseEntity);

        var resp = fileController.getCriminalFileContentSecure(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void getCivilFileContentSecureTest() throws JsonProcessingException {
        var req = new GetCivilFileContentSecure();
        req.setRequestAgencyIdentifierId("A");
        req.setRequestPartId("A");
        req.setRequestDtm(Instant.now());
        req.setAppearanceId(1L);
        req.setCourtLocaCd("A");
        req.setCourtRoomCd("A");
        req.setCourtProceedingDate(Instant.now());
        req.setAppearanceId(1L);
        req.setPhysicalFileId("A");

        var out = new GetCivilFileContentSecureResponse();
        out.setResultCd("A");
        out.setResultMessage("A");

        var doc = new ca.bc.gov.open.jci.civil.secure.CivilFileContentDoc();
        doc.setCourtLocaCd("A");
        doc.setCourtRoomCd("A");
        doc.setCourtProceedingDate(Instant.now());
        doc.getAppearanceId().add("A");
        doc.setPhysicalFileId("A");

        out.setCivilFileContentDoc(doc);

        ResponseEntity<GetCivilFileContentSecureResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetCivilFileContentSecureResponse>>any()))
                .thenReturn(responseEntity);

        var resp = fileController.getCivilFileContentSecure(req);

        Assertions.assertNotNull(resp);
    }
}
