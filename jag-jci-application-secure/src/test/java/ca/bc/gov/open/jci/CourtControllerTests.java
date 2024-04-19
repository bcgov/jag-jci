package ca.bc.gov.open.jci;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.jci.controllers.CourtController;
import ca.bc.gov.open.jci.court.secure.one.BanStatus;
import ca.bc.gov.open.jci.court.secure.one.GetCrtListSecure;
import ca.bc.gov.open.jci.court.secure.one.GetCrtListSecureResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourtControllerTests {

    @Mock private ObjectMapper objectMapper;
    @Mock private RestTemplate restTemplate;
    @Mock private CourtController courtController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        courtController = Mockito.spy(new CourtController(restTemplate, objectMapper));
    }

    @Test
    public void getCrtListSecureTest() throws JsonProcessingException {
        var req = new GetCrtListSecure();
        req.setRequestAgencyIdentifierId("A");
        req.setRequestPartId("A");
        req.setRequestDtm(Instant.now());
        req.setApplicationCd("A");
        req.setAgencyIdentifierCd("A");
        req.setRoomCd("A");
        req.setProceedingDate(Instant.now());
        req.setDivisionCd("A");
        req.setFileNumber("A");

        var out = new GetCrtListSecureResponse();
        out.setResultCd("A");
        out.setResultMessage("A");

        var clt = new ca.bc.gov.open.jci.court.secure.one.CourtListTypes();

        clt.setCourtLocationName("A");
        clt.setCourtRoomCode("A");
        clt.setCourtProceedingsDate(Instant.now());
        var fs = new ca.bc.gov.open.jci.court.secure.one.FileSearchParameter();
        fs.setFileNumber("A");
        fs.setCourtDivisionCd("A");

        clt.setFileSearchParameter(fs);

        var crl = new ca.bc.gov.open.jci.court.secure.one.CriminalCourtList();
        crl.setCriminalAppearanceID("A");
        crl.setCourtListTypeCd("A");
        crl.setAppearanceSequenceNumber("A");
        crl.setAppearanceTime("A");
        var fi = new ca.bc.gov.open.jci.court.secure.one.FileInformationType();
        fi.setFileLocaAgencyIdentifierCd("A");
        fi.setPhyFileFolderNo("A");
        fi.setPhysTicketSeriesTxt("A");
        fi.setMdocInfoSeqNo("A");
        fi.setPartId("A");
        fi.setProfSeqNo("A");
        fi.setMdocJustinNo("A");
        fi.setCourtLevelCd("A");
        fi.setCourtClassCd("A");
        fi.setMdocTypeCd("A");
        fi.setMdocTypeDsc("A");
        fi.setMdocImageId("A");
        fi.setMdocAmendedYN("A");
        fi.setMdocAmendedText("A");
        var is = new ca.bc.gov.open.jci.court.secure.one.Issue();
        is.setCountPrntSeqNo("A");
        is.setStatuteActCd("A");
        is.setStatuteSectionCd("A");
        is.setStatuteSectionDsc("A");
        fi.getIssue().add(is);

        crl.setFileInformation(fi);

        crl.setFileNumberText("A");
        crl.setFileHomeLocationName("A");
        crl.setSealTypeCd("A");
        crl.setSealTypeDsc("A");
        crl.setOtherFileInformationText("A");
        crl.setAccusedFullName("A");

        var fn = new ca.bc.gov.open.jci.court.secure.one.AccusedFormattedName();
        fn.setLastName("A");
        fn.setGiven1Name("A");
        fn.setGiven2Name("A");
        fn.setGiven3Name("A");
        fn.setOrgName("A");

        crl.setAccusedFormattedName(fn);

        crl.setAccusedBirthDate("A");
        crl.setAccusedCurrentBailProcessText("A");
        crl.setAccusedInCustodyFlag("A");
        crl.setCounselFullName("A");
        crl.setCounselDesignationYN("A");
        crl.setCounselPartId("A");
        crl.setCaseAgeDaysNumber("A");
        crl.setCrownTypeCd("A");
        crl.setCrownLocationCd("A");
        crl.setParticipantRoleCd("A");
        crl.setParticipantRoleDsc("A");

        var at = new ca.bc.gov.open.jci.court.secure.one.AttendanceMethod();
        at.setApprId("A");
        at.setAssetUsageSeqNo("A");
        at.setRoleType("A");
        at.setAttendanceMethodCd("A");
        at.setPhoneNumber("A");
        at.setInstruction("A");
        at.setOtherRoleName("A");
        crl.getAttendanceMethod().add(at);

        var ac = new ca.bc.gov.open.jci.court.secure.one.AppearanceCount();
        ac.setAppearanceCountId("A");
        ac.setCountPrintSequenceNumber("A");
        ac.setChargeStatuteCode("A");
        ac.setChargeStatuteDescription("A");
        ac.setLesserIncludedChargeStatuteCode("A");
        ac.setAppearanceCountCancelledYN("A");
        ac.setLesserIncludedChargeStatuteDescription("A");
        ac.setAppearanceReasonCode("A");
        ac.setPleaCode("A");
        ac.setPleaDate(Instant.now());
        ac.setElectionCode("A");
        ac.setElectionDate(Instant.now());
        ac.setMdctSeqNo("A");
        ac.setOffenceAgeDaysNumber("A");
        ac.setIssuingOfficerPoliceForceCode("A");
        ac.setIssuingOfficerPINText("A");
        ac.setIssuingOfficerSurnameName("A");
        ac.setFindingCode("A");
        ac.setFindingDate(Instant.now());

        crl.getAppearanceCount().add(ac);

        var bt = new ca.bc.gov.open.jci.court.secure.one.Bans();
        bt.setBanTypeCd("A");
        bt.setBanTypeDescription("A");
        bt.setBanTypeAct("A");
        bt.setBanTypeSection("A");
        bt.setBanTypeSubSection("A");
        bt.setBanStatuteId("A");
        bt.setBanTypeParagraph("A");
        bt.setBanTypeSubParagraph("A");
        bt.setBanTypeShortDescription("A");
        bt.setBanTypeShortDescription("A");

        BanStatus bs = new ca.bc.gov.open.jci.court.secure.one.BanStatus();
        bs.setBanCommentText("A");
        bs.setBanStatusCd("A");
        bs.setBanStatusDate(Instant.now());
        bs.setBanStatusDsc("A");
        bt.getBanStatus().add(bs);
        crl.getBans().add(bt);

        var bov = new ca.bc.gov.open.jci.court.secure.one.BailOrderToVary();
        bov.setFormTypeCd("A");
        bov.setDocmTypeDsc("A");
        bov.setDocmId("A");
        bov.setDocmIssueDate(Instant.now());
        bov.setDocmImageId("A");
        bov.setDocmStatus("A");
        crl.getBailOrderToVary().add(bov);

        var sov = new ca.bc.gov.open.jci.court.secure.one.SentOrderToVary();
        sov.setFormTypeCd("A");
        sov.setDocmTypeDsc("A");
        sov.setDocmId("A");
        sov.setDocmIssueDate(Instant.now());
        sov.setDocmImageId("A");
        sov.setDocmStatus("A");
        crl.getSentOrderToVary().add(sov);

        var an = new ca.bc.gov.open.jci.court.secure.one.AgeNotice();
        an.setEventDate(Instant.now());
        an.setEventTypeDsc("A");
        an.setDetailText("A");
        an.setDOB(Instant.now());
        an.setRelationship("A");
        an.setProvenBy("A");
        an.setNoticeTo("A");

        crl.getAgeNotice().add(an);

        var st = new ca.bc.gov.open.jci.court.secure.one.SpeakerType();
        st.setSpeakerId("A");
        st.setSpeakerTypeCd("A");
        st.setSpeakerSeqNo("A");
        st.setVoirDireSeqNo("A");
        st.setSpeakerName("A");
        st.setSpeakerStatusCd("A");
        st.setSpeakerStatusDsc("A");
        var se = new ca.bc.gov.open.jci.court.secure.one.SpeakerEvent();
        se.setSpeakerEventDate("A");
        se.setSpeakerEventTime("A");
        se.setSpeakerEventText("A");

        st.getSpeakerEvent().add(se);
        crl.getSpeaker().add(st);

        var aw = new ca.bc.gov.open.jci.court.secure.one.ArrestWarrant();
        aw.setFileNumberText("A");
        aw.setWarrantDate(Instant.now());
        crl.getArrestWarrant().add(aw);

        var crp = new ca.bc.gov.open.jci.court.secure.one.CRProtectionOrderType();
        crp.setPORConditionText("A");
        crp.setOrderTypeDsc("A");
        crp.setPOROrderIssueDate(Instant.now());
        crl.getProtectionOrder().add(crp);

        var sa = new ca.bc.gov.open.jci.court.secure.one.ScheduledAppearance();
        sa.setAppearanceId("A");
        sa.setCourtAgencyIdentifier("A");
        sa.setCourtRoom("A");
        sa.setAppearanceDate(Instant.now());
        sa.setAppearanceTime(Instant.now());
        sa.setAppearanceReasonCd("A");
        sa.setEstDurationHours("A");
        sa.setEstDurationMins("A");

        var hr = new ca.bc.gov.open.jci.court.secure.one.HearingRestrictionType();
        hr.setHearingRestrictiontype("A");
        hr.setJudgeName("A");
        hr.setHearingRestrictionDate(Instant.now());

        crl.getHearingRestriction().add(hr);
        crl.getScheduledAppearance().add(sa);

        var co = new ca.bc.gov.open.jci.court.secure.one.CFCOrderType();
        co.setCFCOrderIssueDate(Instant.now());
        co.setOrderTypeDsc("A");
        co.setCFCConditionText("A");

        crl.getCFCOrder().add(co);

        var crl2 = new ca.bc.gov.open.jci.court.secure.one.CivilCourtListType();
        crl2.setAppearanceId("A");
        crl2.setAppearanceTime("A");
        crl2.setBinderText("A");
        crl2.setCourtListPrintSortNumber("A");
        crl2.setCivilDocumentsAvailable("A");
        crl2.setAppearanceDate(Instant.now());
        crl2.setExternalFileNumberText("A");
        crl2.setCourtListTypeCd("A");
        crl2.setCourtRoomCd("A");
        crl2.setCourtClassCd("A");
        crl2.setFileAccessLevelCd("A");
        crl2.setSealStartDate(Instant.now());
        crl2.setSealEndDate(Instant.now());
        crl2.setSheriffCommentText("A");
        crl2.setSealFileSOCText("A");
        crl2.setEstimatedAppearanceMinutes("A");

        var py = new ca.bc.gov.open.jci.court.secure.one.PhysicalFileType();
        py.setPhysicalFileID("A");
        py.setFileAccessLevelCd("A");
        py.setFileNumber("A");
        py.setStyleOfCause("A");
        py.setLeftPartyLastName("A");
        py.setLeftPartyGivenName("A");
        py.setLeftPartyOtherCount("A");
        py.setRightPartyGivenName("A");
        py.setRightPartyLastName("A");
        py.setRightPartyOtherCount("A");
        py.setThirdPartyGivenName("A");
        py.setThirdPartyLastName("A");
        py.setThirdPartyOtherCount("A");
        py.setHomeAgencyCd("A");
        py.setCivilAgencyCd("A");
        crl2.setPhysicalFile(py);

        var as = new ca.bc.gov.open.jci.court.secure.one.AssetType();
        as.setAssetTypeDescription("A");
        crl2.getAsset().add(as);

        var dt = new ca.bc.gov.open.jci.court.secure.one.DocumentType();
        dt.setAppearanceID("A");
        dt.setDocumentId("A");
        dt.setFileSeqNumber("A");
        dt.setRoleTypeCode("A");
        dt.setDocumentTypeCd("A");
        dt.setDocumentTypeDescription("A");
        dt.setDocumentAccessLevelCd("A");
        dt.setDocumentSealEndDate(Instant.now());
        dt.setDocumentSealStartDate(Instant.now());
        dt.setDateGranted(Instant.now());
        dt.setEstimatedDocumentMinutes("A");
        dt.setDateVaried(Instant.now());
        dt.setCancelledDate(Instant.now());
        dt.setOrderDocumentYN("A");
        dt.setAdjudicatorPartId("A");
        dt.setAdjudicatorName("A");
        dt.setOrderDocumentYN("A");
        dt.setAppearanceReasonCode("A");

        var is2 = new ca.bc.gov.open.jci.court.secure.one.IssueType();
        is2.setIssueDescription("A");
        is2.setIssueType("A");
        is2.setIssueNumber("A");
        dt.getIssue().add(is2);

        var ft = new ca.bc.gov.open.jci.court.secure.one.FiledByType();
        ft.setFiledByName("A");
        ft.setRoleTypeCode("A");
        dt.getFiledBy().add(ft);
        crl2.getDocument().add(dt);

        var pt = new ca.bc.gov.open.jci.court.secure.one.PartyType();
        pt.setPartyId("A");
        pt.setPartyScheduled("A");
        pt.setPartyRoleType("A");
        pt.setPartyFullName("A");
        pt.setAttendanceMethodCd("A");
        pt.setPartyTypeCd("A");
        pt.setLeftRightParty("A");
        pt.setBirthDate(Instant.now());
        pt.setPhoneNumber("A");
        pt.setInstruction("A");
        pt.setPartyFullAddressText("A");
        pt.setWarrantIssueDate("A");
        pt.setActiveYN("A");
        var ct = new ca.bc.gov.open.jci.court.secure.one.CounselType();
        ct.setCounselFullName("A");
        ct.setPhoneNumber("A");
        ct.setCounselId("A");

        pt.getCounsel().add(ct);

        var rt = new ca.bc.gov.open.jci.court.secure.one.RepresentativeType();
        rt.setInstruction("A");
        rt.setAttendanceMethodCd("A");
        rt.setPhoneNumber("A");
        rt.setRepFullName("A");
        pt.getRepresentative().add(rt);

        var lrt = new ca.bc.gov.open.jci.court.secure.one.LegalRepresentativeType();
        lrt.setLegalRepFullName("A");
        lrt.setLegalRepTypeDsc("A");
        pt.getLegalRepresentative().add(lrt);

        var pr2 = new ca.bc.gov.open.jci.court.secure.one.PartyRoleType2();
        pr2.setRoleTypeCd("A");
        pr2.setRoleTypeDsc("A");

        pt.getPartyRole().add(pr2);

        var pnt = new ca.bc.gov.open.jci.court.secure.one.PartyNameType();
        pnt.setFirstGivenNm("A");
        pnt.setNameTypeCd("A");
        pnt.setNameTypeDsc("A");
        pnt.setSurnameNm("A");
        pnt.setFirstGivenNm("A");
        pnt.setSecondGivenNm("A");
        pnt.setThirdGivenNm("A");
        pnt.setOrganizationNm("A");
        pt.getPartyName().add(pnt);

        crl2.getParties().add(pt);

        var st2 = new ca.bc.gov.open.jci.court.secure.one.SpeakerType();
        st2.setSpeakerId("A");
        st2.setSpeakerStatusCd("A");
        st2.setSpeakerSeqNo("A");
        st2.setVoirDireSeqNo("A");
        st2.setSpeakerName("A");
        st2.setSpeakerStatusCd("A");
        st2.setSpeakerStatusDsc("A");
        var se2 = new ca.bc.gov.open.jci.court.secure.one.SpeakerEvent();
        se2.setSpeakerEventText("A");
        se2.setSpeakerEventDate("A");
        se2.setSpeakerEventTime("A");
        st2.getSpeakerEvent().add(se2);

        var pot = new ca.bc.gov.open.jci.court.secure.one.ProtectionOrderType();
        pot.setPOROrderIssueDate(Instant.now());
        pot.setOrderTypeDsc("A");
        pot.setPORConditionText("A");
        pot.setPOROrderExpiryDate(Instant.now());
        var rpt = new ca.bc.gov.open.jci.court.secure.one.RestrainingPartyNameType();
        rpt.setRestrainingPartyName("A");
        var ppt = new ca.bc.gov.open.jci.court.secure.one.ProtectedPartyNameType();
        ppt.setProtectedPartyName("A");
        pot.getRestrainingPartyName().add(rpt);
        pot.getProtectedPartyName().add(ppt);

        crl2.getProtectionOrder().add(pot);

        var sat = new ca.bc.gov.open.jci.court.secure.one.ScheduledAppearanceType();
        sat.setAppearanceId("A");
        sat.setCourtAgencyIdentifier("A");
        sat.setCourtRoom("A");
        sat.setAppearanceDate(Instant.now());
        sat.setAppearanceTime(Instant.now());
        sat.setAppearanceReasonCd("A");
        sat.setEstDurationHours("A");
        sat.setEstDurationMins("A");

        crl2.getScheduledAppearance().add(sat);

        var hr2 = new ca.bc.gov.open.jci.court.secure.one.HearingRestrictionType();
        hr2.setHearingRestrictionDate(Instant.now());
        hr2.setJudgeName("A");
        hr2.setHearingRestrictionDate(Instant.now());
        crl2.getHearingRestriction().add(hr2);

        var caw = new ca.bc.gov.open.jci.court.secure.one.CivilArrestWarrantType();
        caw.setWarrantDate(Instant.now());
        caw.setWarrantTypeCd("A");
        caw.setWarrantTypeDsc("A");
        crl2.getArrestWarrant().add(caw);

        var ud = new ca.bc.gov.open.jci.court.secure.one.UnscheduledDocumentType();
        ud.setDocumentId("A");
        ud.setFileSeqNumber("A");
        ud.setDocumentTypeDsc("A");
        var fb = new ca.bc.gov.open.jci.court.secure.one.FiledByType();
        fb.setRoleTypeCode("A");
        fb.setRoleTypeCode("A");
        ud.getFiledBy().add(fb);

        crl2.getUnscheduledDocument().add(ud);

        var ov = new ca.bc.gov.open.jci.court.secure.one.OrderToVaryType();
        ov.setAdjudicatorName("A");
        ov.setDateGranted(Instant.now());
        ov.setDocumentId("A");
        ov.setDocumentTypeDsc("A");

        crl2.getOrderToVary().add(ov);

        clt.getCivilCourtList().add(crl2);
        clt.getCriminalCourtList().add(crl);
        out.setCourtLists(clt);

        ResponseEntity<GetCrtListSecureResponse> responseEntity =
                new ResponseEntity<>(out, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetCrtListSecureResponse>>any()))
                .thenReturn(responseEntity);

        var resp = courtController.getCrtListSecure(req);

        Assertions.assertNotNull(resp);
    }
}
