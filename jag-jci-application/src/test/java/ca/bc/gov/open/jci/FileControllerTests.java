package ca.bc.gov.open.jci;

import ca.bc.gov.open.jci.civil.*;
import ca.bc.gov.open.jci.common.criminal.file.content.*;
import ca.bc.gov.open.jci.common.criminal.file.content.DocumentType;
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
    public void getCriminalFileContentTest() throws JsonProcessingException {
        var req = new GetCriminalFileContent();
        req.setAgencyIdentifierCd("A");
        req.setAppearanceID("A");
        req.setRoomCd("A");
        req.setMdocJustinNo("A");
        req.setProceedingDate(Instant.now());

        var out = new FileContentDoc();
        out.setCourtLocaCd("A");
        out.setCourtRoomCd("A");
        out.setCourtProceedingDate(Instant.now());
        out.getAppearanceId().add("A");
        out.setMdocJustinNo("A");
        AccusedFileType ac = new AccusedFileType();

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

        ArrestWarrantType wt = new ArrestWarrantType();
        wt.setFileNumberText("A");
        wt.setWarrantDate(Instant.now());
        ac.getArrestWarrant().add(wt);

        BanTypes bt = new BanTypes();
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

        ProtectionOrderType pt = new ProtectionOrderType();
        pt.setPOROrderIssueDate(Instant.now());
        pt.setOrderTypeDsc("A");
        pt.setPORConditionText("A");
        ac.getProtectionOrder().add(pt);

        CFCOrderType ct = new CFCOrderType();
        ct.setCFCOrderIssueDate(Instant.now());
        ct.setCFCConditionText("A");
        ct.setOrderTypeDsc("A");
        ac.getCFCOrder().add(ct);

        HearingRestrictionType ht = new HearingRestrictionType();
        ht.setHearingRestrictiontype("A");
        ht.setJudgeName("A");
        ht.setHearingRestrictionDate(Instant.now());
        ac.getHearingRestriction().add(ht);

        DocumentType dt = new DocumentType();
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

        AppearanceCountTypes apc = new AppearanceCountTypes();
        apc.setAppcId("A");
        apc.setCountNumber("A");
        apc.setAppearanceReason("A");
        apc.setAppearanceResult("A");
        apc.setFinding("A");
        apc.setSectionTxt("A");
        apc.setSectionDscTxt("A");
        SentenceType st = new SentenceType();
        st.setSntpCd("A");
        st.setSentTermPeriodQty("A");
        st.setSentTermCd("A");
        st.setSentSubtermPeriodQty("A");
        st.setSentSubtermCd("A");
        st.setSentTertiaryTermCd("A");
        st.setSentIntermittentYn("A");
        st.setSentMonetaryAmt("A");
        st.setSentDueTtpDt(Instant.now());
        st.setSentEffectiveDt(Instant.now());
        st.setSentDetailTxt("A");
        st.setSentYcjaAdultYouthCd("A");
        st.setSentCustodySecureYn("A");
        apc.getSentence().add(st);
        at.getAppearanceCount().add(apc);

        PartyAppearanceMethodType pmt = new PartyAppearanceMethodType();
        pmt.setPartyName("A");
        pmt.setPartyRole("A");
        pmt.setPartId("A");
        pmt.setPartyAppearanceMethod("A");
        at.getPartyAppearanceMethod().add(pmt);

        out.getAccusedFile().add(ac);

        var outer = new GetCriminalFileContentResponse();
        outer.setFileContent(out);

        ResponseEntity<GetCriminalFileContentResponse> responseEntity =
                new ResponseEntity<>(outer, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetCriminalFileContentResponse>>any()))
                .thenReturn(responseEntity);

        var resp = fileController.getCriminalFileContent(req);

        Assertions.assertNotNull(resp);
    }

    @Test
    public void getCivilFileContentTest() throws JsonProcessingException {
        var req = new GetCivilFileContent();
        req.setCourtLocaCd("A");
        req.setCourtRoomCd("A");
        req.setCourtProceedingDate(Instant.now());
        req.setAppearanceId(1L);
        req.setPhysicalFileId("A");
        req.setApplicationCd("A");

        var fileContentDoc = new CivilFileContentDoc();
        fileContentDoc.setCourtLocaCd("A");
        fileContentDoc.setCourtRoomCd("A");
        fileContentDoc.setCourtProceedingDate(Instant.now());
        fileContentDoc.getAppearanceId().add("A");
        fileContentDoc.setPhysicalFileId("A");

        var out = new GetCivilFileContentResponse();
        out.setCivilFileContentDoc(fileContentDoc);

        var cft = new CivilFileType();
        cft.setPhysicalFileID("A");
        cft.setFileNumber("A");
        cft.setCourtLevelCd("A");
        cft.setCourtClassCd("A");
        cft.setIndigencyGrantedYN("A");
        cft.setInfantFileYN("A");
        cft.setCFCSAFileYN("A");
        cft.setMarriageDate(Instant.now());
        cft.setMarriageDate(Instant.now());
        cft.setDivorceDate(Instant.now());
        cft.setFederalClearanceDate(Instant.now());
        cft.setCentralRegNumber("A");
        cft.setCaveatExpiryDate(Instant.now());
        cft.setProbateFileNumber("A");
        cft.setCaveatCancelledYN("A");
        cft.setAssetDeclaredAmt("A");
        cft.setProbateFeeAmt("A");
        cft.setProbateEnteredDate(Instant.now());
        cft.setAdditionalAssetDeclaredAmt("A");
        cft.setAdditionalProbateFeeAmt("A");
        cft.setEstateValueAmt("A");
        cft.setOutsideBCAssetAmt("A");
        cft.setAssetCommentText("A");
        cft.setFileCommentText("A");
        cft.setSealedYN("A");
        cft.setSheriffCommentText("A");
        cft.setFileLocaAgencyIdentifierCd("A");

        var sp = new SpecialProgramType();
        sp.setSpecialProgramDsc("A");
        sp.setProgramEntryDate(Instant.now());
        sp.setProgramEntryReasonDsc("A");
        sp.setProgramExitDate(Instant.now());
        sp.setProgramExitReasonDsc("A");

        cft.getSpecialProgram().add(sp);

        var pt = new PreviousAppearanceType();
        pt.setAppearanceId("A");
        pt.setAppearanceDate(Instant.now());
        pt.setCourtAgencyIdentifier("A");
        pt.setCourtRoom("A");
        pt.setAdjudicatorName("A");
        pt.setAdjudicatorComment("A");
        pt.setAdjudicatorAppearanceMethod("A");
        cft.getPreviousAppearance().add(pt);

        var dh = new DocumentHearingType();
        dh.setDocumentId("A");
        dh.setFileSeqNumber("A");
        dh.setDocumentTypeCd("A");
        dh.setDocumentTypeDescription("A");
        dh.setOrderDocumentYN("A");
        dh.setAppearanceReasonCode("A");
        dh.setAppearanceResultCode("A");
        var is = new IssueType();
        is.setIssueDsc("A");
        is.setIssueNumber("A");
        is.setConcludedYN("A");
        dh.getIssue().add(is);

        var fb = new FiledByType();
        fb.setFiledByName("A");
        fb.setRoleTypeCode("A");
        dh.getFiledBy().add(fb);

        var cp = new CourtParticipantType();
        cp.setPartyName("A");
        cp.setPartyRole("A");
        cp.setPartId("A");
        cp.setPartyAppearanceMethod("A");
        cp.setLeftRightParty("A");
        var ct = new CounselType();
        ct.setCounselName("A");
        ct.setCounselAppearanceMethod("A");
        cp.getCounsel().add(ct);

        pt.getCourtParticipant().add(cp);
        pt.getDocumentHearing().add(dh);

        var gt = new GeneralAttendeeType();
        gt.setAttendeeName("A");
        gt.setCounselName("A");

        pt.getGeneralAttendee().add(gt);

        var dt = new ca.bc.gov.open.jci.civil.DocumentType();
        dt.setDocumentId("A");
        dt.setFileSeqNumber("A");
        dt.setDocumentTypeCd("A");
        dt.setDocumentTypeDescription("A");
        dt.setDocumentAccessLevelCd("A");
        dt.setDocumentSealEndDate(Instant.now());
        dt.setDocumentSealStartDate(Instant.now());
        dt.setDateGranted(Instant.now());
        dt.setEstimatedDocumentMinutes("A");
        dt.setEndedDate(Instant.now());
        dt.setDateVaried(Instant.now());
        dt.setCancelledDate(Instant.now());
        dt.setDocumentCommentText("A");
        dt.setOrderDocumentYN("A");
        dt.setPOROrderYN("A");
        dt.setCFCOrderYN("A");
        dt.setImageId("A");
        dt.setSealedYN("A");
        dt.setDocumentPageCount("A");
        dt.setSwornByNm("A");
        dt.setAffidavitNo("A");

        var is2 = new IssueType();
        is2.setIssueDsc("A");
        is2.setConcludedYN("A");
        is2.setIssueNumber("A");
        dt.getIssue().add(is2);

        var pit = new PartyInterestType();
        pit.setOrderRoleTypeDsc("A");
        pit.setPartyName("A");
        pit.setPartyBirthDate(Instant.now());
        dt.getPartyInterest().add(pit);

        cft.getDocument().add(dt);

        var rd = new ReferenceDocumentType();
        rd.setReferenceDocumentId("A");
        rd.setReferenceDocumentNo("A");
        rd.setAppearanceId("A");
        rd.setAppearanceDate(Instant.now());
        rd.setObjectGuid("A");
        rd.setDescriptionText("A");
        rd.setEnterDtm(Instant.now());
        rd.setReferenceDocumentTypeCd("A");
        rd.setReferenceDocumentTypeDsc("A");
        var rdi = new ReferenceDocInterest();
        rdi.setPartyId("A");
        rdi.setPartyName("A");
        rdi.setNonPartyName("A");
        rd.getReferenceDocumentInterest().add(rdi);

        ResponseEntity<GetCivilFileContentResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetCivilFileContentResponse>>any()))
                .thenReturn(responseEntity);

        var resp = fileController.getCivilFileContent(req);

        Assertions.assertNotNull(resp);
    }
}
