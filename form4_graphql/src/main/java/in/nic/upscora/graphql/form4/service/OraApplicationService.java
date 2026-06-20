package in.nic.upscora.graphql.form4.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.nic.upscora.graphql.form4.cache.MasterDataCache;
import in.nic.upscora.graphql.form4.cache.MasterDataConstants;
import in.nic.upscora.graphql.form4.exceptions.ApplicationNotFoundException;
import in.nic.upscora.graphql.form4.mongo.entity.application.OraApplication;
import in.nic.upscora.graphql.form4.mongo.entity.application.OraApplicationAudit;
import in.nic.upscora.graphql.form4.mongo.entity.application.PaymentDetails;
import in.nic.upscora.graphql.form4.mongo.entity.caf.CandidateProfile;
import in.nic.upscora.graphql.form4.mongo.entity.caf.PreIdentity;
import in.nic.upscora.graphql.form4.mongo.entity.userprofile.UserProfile;
import in.nic.upscora.graphql.form4.mongo.repository.CandidateRepository;
import in.nic.upscora.graphql.form4.mongo.repository.OraApplicationAuditRepository;
import in.nic.upscora.graphql.form4.mongo.repository.OraApplicationRepository;
import in.nic.upscora.graphql.form4.mongo.repository.UserProfileRepository;
import in.nic.upscora.graphql.form4.request.AdditionalExperience;
import in.nic.upscora.graphql.form4.request.AdditionalQualification;
import in.nic.upscora.graphql.form4.request.AdvertisementInput;
import in.nic.upscora.graphql.form4.request.AgeRelaxation;
import in.nic.upscora.graphql.form4.request.CertificateInput;
import in.nic.upscora.graphql.form4.request.Conference;
import in.nic.upscora.graphql.form4.request.DesirableExperience;
import in.nic.upscora.graphql.form4.request.DesirableQualification;
import in.nic.upscora.graphql.form4.request.Diploma;
import in.nic.upscora.graphql.form4.request.EssentialExperience;
import in.nic.upscora.graphql.form4.request.EssentialQualification;
import in.nic.upscora.graphql.form4.request.ExamCenter;
import in.nic.upscora.graphql.form4.request.Gate;
import in.nic.upscora.graphql.form4.request.Internship;
import in.nic.upscora.graphql.form4.request.Language;
import in.nic.upscora.graphql.form4.request.Miscellaneous;
import in.nic.upscora.graphql.form4.request.Net;
import in.nic.upscora.graphql.form4.request.Ora_Applicant;
import in.nic.upscora.graphql.form4.request.OtherDocs;
import in.nic.upscora.graphql.form4.request.PaymentInput;
import in.nic.upscora.graphql.form4.request.ProfessionalReg;
import in.nic.upscora.graphql.form4.request.ProfileLock;
import in.nic.upscora.graphql.form4.request.Publication;
import in.nic.upscora.graphql.form4.request.SubmitApplication;
import in.nic.upscora.graphql.form4.response.CommonCandidateReportRecord;
import in.nic.upscora.graphql.form4.response.EligibilityResponse;
import in.nic.upscora.graphql.form4.response.OraApplicationReportRecord;
import in.nic.upscora.graphql.form4.response.PaymentDetailsResponse;
import in.nic.upscora.graphql.form4.response.UserProfileResponse;
import in.nic.upscora.graphql.form4.utils.ApplicationIdGenerator;
import in.nic.upscora.graphql.form4.utils.OraApplicationHelper;
import in.nic.upscora.graphql.form4.utils.OraApplicationUtil;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.graphql.GraphQLException;
import io.quarkus.cache.CacheInvalidateAll;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class OraApplicationService {

    private static final String PAYMENT_DETAILS_RESPONSE_LOG = "getPaymentDetailsByUrnAndRecruitmentCode | response={}";
    private static final String DELETE_ACC_RESPONSE_LOG = "deleteAccount | response={}";
    private static final String SUBMITTED = "SUBMITTED";

    private static final String APPLICATION_SUBMITTED_UPDATE_ERR = "Application status is Submitted. Cannot update";
    private static final String APPLICANT_URN_ERR = "Applicant URN Does Not exist. Cannot update";

    private static final String UNKNOWN = "Unknown";

    private final OraApplicationAuditRepository auditRepo;
    private final CandidateRepository cafRepo;
    private final UserProfileRepository userRepo;
    private final OraApplicationRepository oraRepo;
    private final ApplicationIdGenerator idgenerator;
    private final MasterDataCache masterDataCache;

    private final OraApplicationHelper oraApplicationHelper;

    public OraApplicationService(OraApplicationAuditRepository auditRepo, CandidateRepository cafRepo,
            UserProfileRepository userRepo, ApplicationIdGenerator idgenerator,
            MasterDataCache masterDataCache, OraApplicationHelper oraApplicationHelper,
            OraApplicationRepository oraRepo) {
        this.auditRepo = auditRepo;
        this.cafRepo = cafRepo;
        this.userRepo = userRepo;
        this.idgenerator = idgenerator;
        this.masterDataCache = masterDataCache;
        this.oraApplicationHelper = oraApplicationHelper;
        this.oraRepo = oraRepo;
    }

    @ConfigProperty(name = "ngrp.api.type")
    String ngrpType;

    @ConfigProperty(name = "ngrp.api.caf.type")
    String cafNgrpType;

    @ConfigProperty(name = "ngrp.api.lang", defaultValue = "en")
    String ngrpLang;

    public List<OraApplication> getAllApplicants() {
        log.info("getAllApplicants | request=fetch-all");
        List<OraApplication> response = oraRepo.findAllApplicants();
        log.info("getAllApplicants | resultCount={} | response={}", response.size(),
                oraApplicationHelper.safeJson(response));
        return response;
    }

    public OraApplication getApplicationByUrnAndVacany(String urn, String vacancyId) {
        log.info("getApplicationByUrnAndVacany | request={{\"applicant_urn\":\"{}\",\"vacancyId\":\"{}\"}}", urn,
                vacancyId);

        OraApplication oraApplication = oraRepo.findByURNAndVacancy(urn, vacancyId);
        if (oraApplication == null) {
            log.error("getApplicationByUrnAndVacany | status=NOT_FOUND | applicantUrn={} | vacancyId={}", urn,
                    vacancyId);
            throw new ApplicationNotFoundException(
                    "Application not found with Applicant URN: " + urn + " and Vacancy ID: " + vacancyId);
        }

        log.info(
                "getApplicationByUrnAndVacany | status=FOUND | applicationStatus={} | cafLocked={} | profileLocked={} | response={}",
                OraApplicationUtil.getApplicationInfoStatus(oraApplication),
                OraApplicationUtil.getCafLocked(oraApplication),
                OraApplicationUtil.getProfileLocked(oraApplication),
                oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;
    }

    public boolean getProfileLocked(Ora_Applicant applicantInfo) {
        log.info("getProfileLocked | request={}", oraApplicationHelper.safeJson(applicantInfo));
        String applicantURN = applicantInfo.getApplicant_info().getApplicant_urn();
        String vacancyId = applicantInfo.getApplicant_info().getVacancyId();
        OraApplication oraApplication = getApplicationByUrnAndVacany(applicantURN, vacancyId);

        if (oraApplication.getApplication_info() == null) {
            log.info("getProfileLocked | status=NO_APPLICATION_INFO | response=false");
            return false;
        }

        boolean response = oraApplication.getApplication_info().isProfileLocked();
        log.info("getProfileLocked | status=SUCCESS | profileLocked={} | applicantUrn={} | vacancyId={}",
                response,
                applicantURN,
                vacancyId);
        return response;
    }

    public List<PaymentDetailsResponse> getPaymentDetailsByUrnAndRecruitmentCode(String urn, String recruitmentCode) {
        log.info(
                "getPaymentDetailsByUrnAndRecruitmentCode | request={{\"applicant_urn\":\"{}\",\"recruitmentCode\":\"{}\"}}",
                urn,
                recruitmentCode);
        log.info("Fetching Payment Details for URN: {} and Recruitment Code: {}", urn, recruitmentCode);
        OraApplication oraApplication = oraRepo.findByURNAndRecruitmentCode(urn, recruitmentCode);

        if (oraApplication == null) {
            log.error("Application not found for URN: {} and Recruitment Code: {}", urn, recruitmentCode);
            List<PaymentDetailsResponse> response = new ArrayList<>();
            log.info(PAYMENT_DETAILS_RESPONSE_LOG, oraApplicationHelper.safeJson(response));
            return response;
        }

        PaymentDetails pmt = oraApplication.getPayment_details();
        if (pmt == null) {
            log.warn("No Payment Details found for URN: {} and Recruitment Code: {}", urn, recruitmentCode);
            List<PaymentDetailsResponse> response = new ArrayList<>();
            log.info(PAYMENT_DETAILS_RESPONSE_LOG, oraApplicationHelper.safeJson(response));
            return response;
        }

        List<PaymentDetailsResponse> response = List.of(PaymentDetailsResponse.builder()
                .amount(pmt.getAmount())
                .transaction_date(pmt.getTransaction_date())
                .transaction_id(pmt.getTransaction_id())
                .payment_mode(pmt.getPayment_mode())
                .build());
        log.info(PAYMENT_DETAILS_RESPONSE_LOG, oraApplicationHelper.safeJson(response));
        return response;
    }

    public List<OraApplication> getAllSubmittedExamsByUrn(String urn) {
        log.info("getAllSubmittedExamsByUrn | request={{\"applicant_urn\":\"{}\"}}", urn);
        log.info("Fetching all submitted applications for URN: {}", urn);
        List<OraApplication> submittedApplications = oraRepo.findAllSubmittedByURN(urn);

        if (submittedApplications == null || submittedApplications.isEmpty()) {
            log.warn("No submitted applications found for URN: {}", urn);
            List<OraApplication> response = new ArrayList<>();
            log.info("getAllSubmittedExamsByUrn | response={}", oraApplicationHelper.safeJson(response));
            return response;
        }

        log.info("Found {} submitted applications for URN: {}", submittedApplications.size(), urn);
        log.info("getAllSubmittedExamsByUrn | response={}", oraApplicationHelper.safeJson(submittedApplications));
        return submittedApplications;
    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication createApplication(Ora_Applicant newOraApplication, boolean testing) throws GraphQLException {
        log.info("addApplicant | request={}", oraApplicationHelper.safeJson(newOraApplication));

        String applicantUrn = newOraApplication.getApplicant_info().getApplicant_urn();
        String vacancyId = newOraApplication.getApplicant_info().getVacancyId();
        String recruitmentCode = newOraApplication.getApplicant_info().getRecruitmentCode();

        OraApplication oraApplication = new OraApplication();

        if (oraRepo.findByURNAndVacancy(applicantUrn, vacancyId) == null) {
            processByVacancyAndUrn(newOraApplication, oraApplication, applicantUrn, vacancyId, recruitmentCode,
                    testing);

        } else {
            oraApplication = getApplicationByUrnAndVacany(applicantUrn, vacancyId);
            processByExistingUrn(oraApplication, applicantUrn, vacancyId, recruitmentCode, testing);
        }
        log.info(
                "addApplicant | finalStatus=SUCCESS | applicationStatus={} | cafLocked={} | profileLocked={} | response={}",
                OraApplicationUtil.getApplicationInfoStatus(oraApplication),
                OraApplicationUtil.getCafLocked(oraApplication),
                OraApplicationUtil.getProfileLocked(oraApplication),
                oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    private void processByExistingUrn(OraApplication oraApplication,
            String applicantUrn, String vacancyId, String recruitmentCode, boolean testing) throws GraphQLException {

        log.info("addApplicant | mode=UPDATE_EXISTING | applicantUrn={} | vacancyId={}", applicantUrn, vacancyId);
        log.info("addApplicant | existingStatus={} | cafLocked={} | profileLocked={} | existing={}",
                OraApplicationUtil.getApplicationInfoStatus(oraApplication),
                OraApplicationUtil.getCafLocked(oraApplication),
                OraApplicationUtil.getProfileLocked(oraApplication),
                oraApplicationHelper.safeJson(oraApplication));

        if (!isApplicationEditable(oraApplication.getApplication_info().getStatus())) {
            log.info("addApplicant | updateBlocked=SUBMITTED | applicantUrn={} | vacancyId={} | currentStatus={}",
                    applicantUrn,
                    vacancyId,
                    OraApplicationUtil.getApplicationInfoStatus(oraApplication));
            throw new ApplicationNotFoundException(APPLICATION_SUBMITTED_UPDATE_ERR);
        }

        if (oraApplication.getApplication_info().isCafLocked()) {
            log.info(
                    "addApplicant | updateBlocked=CAF_LOCKED | applicantUrn={} | vacancyId={} | currentStatus={} | cafLocked=true",
                    applicantUrn,
                    vacancyId,
                    OraApplicationUtil.getApplicationInfoStatus(oraApplication));
            throw new ApplicationNotFoundException(
                    "CAF is locked for Applicant URN: " + applicantUrn + ". Cannot update");
        }

        CandidateProfile candidateProfile = getCandidateProfile(applicantUrn);

        oraApplication.setCaf_details(candidateProfile);
        oraApplication.getApplication_info().setCafLocked(true);
        oraApplication.setTesting(testing);
        oraRepo.update(oraApplication);
        log.info(
                "addApplicant | updateStatus=SUCCESS | applicationStatus={} | cafLocked={} | profileLocked={} | updated={}",
                OraApplicationUtil.getApplicationInfoStatus(oraApplication),
                OraApplicationUtil.getCafLocked(oraApplication),
                OraApplicationUtil.getProfileLocked(oraApplication),
                oraApplicationHelper.safeJson(oraApplication));
        try {
            oraApplicationHelper.copyDocsToExamBucket(applicantUrn, recruitmentCode, candidateProfile);
        } catch (GraphQLException e) {
            if ("no caf document found".equalsIgnoreCase(e.getMessage())) {
                throw new ApplicationNotFoundException("no caf document found");
            }
            throw e;
        }
        // Send CAF Locked Notification
        UserProfile userProfile = userRepo.findById(Long.parseLong(applicantUrn));
        if (userProfile != null) {
            oraApplicationHelper.sendCafLockedNotifications(userProfile, oraApplication);
        }
    }

    private void processByVacancyAndUrn(Ora_Applicant newOraApplication, OraApplication oraApplication,
            String applicantUrn, String vacancyId, String recruitmentCode, boolean testing) throws GraphQLException {

        log.info("addApplicant | mode=CREATE_NEW | applicantUrn={} | vacancyId={} | recruitmentCode={}",
                applicantUrn,
                vacancyId,
                recruitmentCode);

        CandidateProfile candidateProfile = getCandidateProfile(applicantUrn);

        Long applicationId = idgenerator.generateId();

        log.info("addApplicant | generatedApplicationId={} | applicantUrn={} | vacancyId={}",
                applicationId,
                newOraApplication.getApplicant_info().getApplicant_urn(),
                vacancyId);
        oraApplication.setValues(newOraApplication.getApplicant_info(), applicationId, candidateProfile);
        oraApplication.setStatus("DRAFT");
        oraApplication.setSubmitDeclarationAccepted(false);
        oraApplication.setCenterDeclarationAccepted(false);
        oraApplication.setTesting(testing);
        oraApplication.setCreated_at(OraApplicationUtil.nowIst());
        oraRepo.persist(oraApplication);
        log.info(
                "addApplicant | persistStatus=SUCCESS | applicationStatus={} | cafLocked={} | profileLocked={} | persisted={}",
                OraApplicationUtil.getApplicationInfoStatus(oraApplication),
                OraApplicationUtil.getCafLocked(oraApplication),
                OraApplicationUtil.getProfileLocked(oraApplication),
                oraApplicationHelper.safeJson(oraApplication));
        try {
            oraApplicationHelper.copyDocsToExamBucket(applicantUrn, recruitmentCode, candidateProfile);
        } catch (GraphQLException e) {
            if ("no caf document found".equalsIgnoreCase(e.getMessage())) {
                throw new ApplicationNotFoundException("no caf document found");
            }
            throw e;
        }
        // Send CAF Locked Notification
        UserProfile userProfile = userRepo.findById(Long.parseLong(applicantUrn));
        if (userProfile != null) {
            oraApplicationHelper.sendCafLockedNotifications(userProfile, oraApplication);
        }
    }

    private CandidateProfile getCandidateProfile(String urn) {
        Long cafUrn = Long.parseLong(urn);

        log.info("URN converted to long : {}" + cafUrn);
        log.info("URN as string : {}" + urn);

        CandidateProfile cafProfile;
        try {
            cafProfile = cafRepo.findById(cafUrn);
        } catch (Exception e) {
            log.error("Error fetching Candidate Profile for Applicant Long URN: {}", cafUrn, e);
            throw new ApplicationNotFoundException(
                    "Error fetching Candidate Profile with Application URN: " + cafUrn);
        }
        if (cafProfile == null) {
            log.error("Candidate Profile not found for Applicant Long URN: {}", cafUrn);

            throw new ApplicationNotFoundException("Candidate Profile not found with Application URN: " + cafUrn);
        }

        log.info("Candidate Profile found for Applicant URN: {} : ", cafUrn);
        return cafProfile;
    }

    // Update Code Start

    private OraApplication processApplicationUpdate(
            String urn,
            String vacancyId,
            String stepKey,
            boolean requireProfileUnlocked,
            Consumer<OraApplication> specificUpdates) {

        OraApplication oraApplication = getApplicationByUrnAndVacany(urn, vacancyId);

        if (oraApplication == null) {
            log.info(APPLICANT_URN_ERR);
            throw new ApplicationNotFoundException(APPLICANT_URN_ERR);
        }

        if (!isApplicationEditable(oraApplication.getApplication_info().getStatus())) {
            log.info(APPLICATION_SUBMITTED_UPDATE_ERR);
            throw new ApplicationNotFoundException(APPLICATION_SUBMITTED_UPDATE_ERR);
        }

        if (requireProfileUnlocked) {
            ensureProfileUnlocked(oraApplication);
        }

        // Execute the unique updates passed via lambda
        specificUpdates.accept(oraApplication);

        // Apply common updates and save
        if (stepKey != null && oraApplication.getApplication_info() != null) {
            oraApplication.getApplication_info().setStepKey(stepKey);
        }
        oraApplication.setUpdated_at(OraApplicationUtil.nowIst());
        oraRepo.update(oraApplication);

        return oraApplication;
    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantAgeRelaxation(AgeRelaxation updateRequest) {

        log.info("updateApplicantAgeRelaxation | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> {
                    app.setClaimAgeRelaxation(updateRequest.getClaimAgeRelaxation());
                    app.setRelaxationCategoryCode(updateRequest.getRelaxationCategoryCode());
                    app.setSupportDocument(updateRequest.getSupportDocument());
                    app.setBiggerFontSize(updateRequest.getBiggerFontSize());
                    app.setCompensatoryTime(updateRequest.getCompensatoryTime());
                    app.setWantScribe(updateRequest.getWantScribe());
                    app.setAssistiveDevice(updateRequest.getAssistiveDevice());
                    app.setAssistiveDevices(updateRequest.getAssistiveDevices());
                    app.setUpdated_at(OraApplicationUtil.nowIst());
                });

        log.info("updateApplicantAgeRelaxation | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantEssentialQualification(EssentialQualification updateRequest) {
        log.info("updateApplicantEssentialQualification | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> app.setEssential_qualification(updateRequest.getEssential_qualification()));

        log.info("updateApplicantEssentialQualification | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantEssentialExperience(EssentialExperience updateRequest) {
        log.info("updateApplicantEssentialExperience | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> {
                    oraApplicationHelper.applyExperienceCalculationDate(
                            updateRequest.getApplicant_info().getVacancyId(),
                            updateRequest.getEssential_experience());
                    app.setEssential_experience(updateRequest.getEssential_experience());
                });

        log.info("updateApplicantEssentialExperience | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantDesirableQualification(DesirableQualification updateRequest) {

        log.info("updateApplicantDesirableQualification | request={}", oraApplicationHelper.safeJson(updateRequest));
        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> {
                    app.setDesirableQualificationApplicable(updateRequest.getDesirableQualificationApplicable());
                    app.setDesirable_qualification(updateRequest.getDesirable_qualification());

                });

        log.info("updateApplicantDesirableQualification | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantDesirableExperience(DesirableExperience updateRequest) {
        log.info("updateApplicantDesirableExperience | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> {
                    oraApplicationHelper.applyExperienceCalculationDate(
                            updateRequest.getApplicant_info().getVacancyId(),
                            updateRequest.getDesirable_experience());
                    app.setDesirableExperienceApplicable(updateRequest.getDesirableExperienceApplicable());
                    app.setDesirable_experience(updateRequest.getDesirable_experience());
                });

        log.info("updateApplicantDesirableExperience | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantAdditionalQualification(AdditionalQualification updateRequest) {

        log.info("updateApplicantAdditionalQualification | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> {
                    app.setAdditionalQualificationApplicable(updateRequest.getAdditionalQualificationApplicable());
                    app.setAdditional_qualification(updateRequest.getAdditional_qualification());
                });

        log.info("updateApplicantAdditionalQualification | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantAdditionalExperience(AdditionalExperience updateRequest) {

        log.info("updateApplicantAdditionalExperience | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> {
                    oraApplicationHelper.applyExperienceCalculationDate(
                            updateRequest.getApplicant_info().getVacancyId(),
                            updateRequest.getAdditional_experience());
                    app.setAdditionalExperienceApplicable(updateRequest.getAdditionalExperienceApplicable());
                    app.setAdditional_experience(updateRequest.getAdditional_experience());
                });

        log.info("updateApplicantAdditionalExperience | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantNet(Net updateRequest) {
        log.info("updateApplicantNet | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> app.setNetQualifications(updateRequest.getNetQualifications()));

        log.info("updateApplicantNet | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantGate(Gate updateRequest) {

        log.info("updateApplicantGate | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> app.setGateQualifications(updateRequest.getGateQualifications()));

        log.info("updateApplicantGate | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantDiploma(Diploma updateRequest) {
        log.info("updateApplicantDiploma | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> {
                    app.setDiplomaApplicable(updateRequest.getDiplomaApplicable());
                    app.setDiplomas(updateRequest.getDiplomas());
                });

        log.info("updateApplicantDiploma | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantCertificate(CertificateInput updateRequest) {
        log.info("updateApplicantCertificate | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> app.setCertificates(updateRequest.getCertificates()));

        log.info("updateApplicantCertificate | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantInternship(Internship updateRequest) {
        log.info("updateApplicantInternship | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> app.setInternships(updateRequest.getInternships()));

        log.info("updateApplicantInternship | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantPublication(Publication updateRequest) {
        log.info("updateApplicantPublication | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> app.setPublications(updateRequest.getPublications()));

        log.info("updateApplicantPublication | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantConference(Conference updateRequest) {
        log.info("updateApplicantConference | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> app.setConferences(updateRequest.getConferences()));

        log.info("updateApplicantConference | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantProfessionalReg(ProfessionalReg updateRequest) {
        log.info("updateApplicantProfessionalReg | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> app.setRegistrations(updateRequest.getRegistrations()));

        log.info("updateApplicantProfessionalReg | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantLanguage(Language updateRequest) {
        log.info("updateApplicantLanguage | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> app.setLanguages(updateRequest.getLanguages()));

        log.info("updateApplicantLanguage | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantMiscellaneous(Miscellaneous updateRequest) {
        log.info("updateApplicantMiscellaneous | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> app.setAnswers(updateRequest.getAnswers()));

        log.info("updateApplicantMiscellaneous | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantOtherDocs(OtherDocs updateRequest) {
        log.info("updateApplicantOtherDocs | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), true,
                app -> {
                    app.setOtherDocsApplicable(updateRequest.getOtherDocsApplicable());
                    app.setMergedDocument(updateRequest.getMergedDocument());
                });

        log.info("updateApplicantOtherDocs | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantExamCenter(ExamCenter updateRequest) {
        log.info("updateApplicantExamCenter | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), false,
                app -> {
                    app.setCenterPreferences(updateRequest.getCenterPreferences());
                    app.getApplication_info().setCenterDeclarationAccepted(true);
                    app.setCenterDeclarationAccepted(true);
                });

        log.info("updateApplicantExamCenter | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantProfileLock(ProfileLock updateRequest) {
        log.info("updateApplicantProfileLock | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), null, false,
                app -> app.getApplication_info().setProfileLocked(true));

        log.info("updateApplicantProfileLock | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;
    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication unlockProfile(ProfileLock updateRequest) {

        log.info("unlockProfile | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), null, false,
                app -> app.getApplication_info().setProfileLocked(false));

        log.info("unlockProfile | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;
    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication updateApplicantPayment(PaymentInput updateRequest) {
        log.info("updateApplicantPayment | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(), false,
                app -> app.setPayment_details(updateRequest.getPayment_details()));

        log.info("updateApplicantPayment | response={}", oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public OraApplication submitApplication(SubmitApplication updateRequest) {

        log.info("submitApplication | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = processApplicationUpdate(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId(), updateRequest.getApplicant_info().getStepKey(),
                false,
                app -> {
                    app.getApplication_info()
                            .setSubmitDeclarationAccepted(updateRequest.isSubmitDeclarationAccepted());
                    app.getApplication_info().setSubmittedAt(OraApplicationUtil.nowIst());
                    app.getApplication_info().setStatus(SUBMITTED);
                    app.setStatus(SUBMITTED);
                    app.setSubmitDeclarationAccepted(true);
                    app.setSubmittedAt(OraApplicationUtil.nowIstString());
                });

        // Send Final Submission Notification
        UserProfile userProfile = userRepo
                .findById(Long.parseLong(updateRequest.getApplicant_info().getApplicant_urn()));
        if (userProfile != null) {
            oraApplicationHelper.sendFinalSubmissionNotifications(userProfile, oraApplication);
        }

        log.info(
                "submitApplication | finalStatus=SUCCESS | applicationId={} | applicantUrn={} | vacancyId={} | finalStatus={} | submitDeclarationAccepted={} | submittedAt={} | response={}",
                oraApplication.getApplicationId(),
                oraApplication.getApplicant_urn(),
                oraApplication.getVacancyId(),
                OraApplicationUtil.getApplicationInfoStatus(oraApplication),
                oraApplication.isSubmitDeclarationAccepted(),
                oraApplication.getSubmittedAt(),
                oraApplicationHelper.safeJson(oraApplication));
        return oraApplication;

    }

    public boolean isApplicationEditable(String status) {
        log.info("isApplicationEditable | currentStatus={}", status);

        if ((status.equals(SUBMITTED))) {
            log.info("isApplicationEditable | editable=false");
            return false;
        }

        log.info("isApplicationEditable | editable=true");
        return true;
    }

    private void ensureProfileUnlocked(OraApplication oraApplication) {
        if (oraApplication.getApplication_info() != null
                && oraApplication.getApplication_info().isProfileLocked()) {
            log.info("Profile is locked for Applicant URN {}. Cannot update", oraApplication.getApplicant_urn());
            throw new ApplicationNotFoundException("Profile is locked. Cannot update");
        }
    }

    public EligibilityResponse checkCafUnlockEligibility(Ora_Applicant updateRequest) {
        log.info("unlockCaf | request={}", oraApplicationHelper.safeJson(updateRequest));

        OraApplication oraApplication = getApplicationByUrnAndVacany(
                updateRequest.getApplicant_info().getApplicant_urn(),
                updateRequest.getApplicant_info().getVacancyId());

        if (oraApplication == null) {
            log.info(APPLICANT_URN_ERR);
            throw new ApplicationNotFoundException(APPLICANT_URN_ERR);
        }

        if (!isApplicationEditable(oraApplication.getApplication_info().getStatus())) {
            log.info(APPLICATION_SUBMITTED_UPDATE_ERR);
            throw new ApplicationNotFoundException(APPLICATION_SUBMITTED_UPDATE_ERR);
        }

        String requestStepKey = updateRequest.getApplicant_info().getStepKey();
        String currentStepKey = oraApplication.getApplication_info().getStepKey();

        EligibilityResponse eligibilityResponse = new EligibilityResponse();

        if ("check-unlock".equalsIgnoreCase(requestStepKey)) {
            switch (currentStepKey) {
                case "exam-payment":
                    break;
                case "final-submit":
                    eligibilityResponse.setEligibleForUnlock(false);
                    eligibilityResponse
                            .setMessage("Cannot Unlock CAF now. Steps Exceeded. Current Step: " + currentStepKey);
                    break;
                default:
                    eligibilityResponse.setEligibleForUnlock(true);
                    eligibilityResponse.setMessage("Eligible for CAF Unlock. Current Step: " + currentStepKey);
            }
            log.info("unlockCaf | status=CHECK_ONLY | currentStepKey={} | response={}", currentStepKey,
                    oraApplicationHelper.safeJson(eligibilityResponse));
            return eligibilityResponse;
        }

        switch (currentStepKey) {

            case "exam-payment":
                break;
            case "final-submit":
                eligibilityResponse.setEligibleForUnlock(false);
                eligibilityResponse
                        .setMessage("Cannot Unlock CAF now. Steps Exceeded. Current Step: " + currentStepKey);
                break;
            default:
                oraApplication.getApplication_info().setCafLocked(false);
                try {
                    OraApplicationAudit.AuditEntry entry = OraApplicationAudit.AuditEntry.builder()
                            .oraApplication(oraApplication)
                            .deleted_at(OraApplicationUtil.nowIst())
                            .build();
                    OraApplicationAudit audit = auditRepo.findByApplicantUrnAndVacancy(
                            oraApplication.getApplicant_urn(),
                            oraApplication.getVacancyId());
                    if (audit == null) {
                        audit = OraApplicationAudit.builder()
                                .applicant_urn(oraApplication.getApplicant_urn())
                                .vacancyId(oraApplication.getVacancyId())
                                .build();
                        audit.addEntry(entry);
                        auditRepo.persist(audit);
                    } else {
                        audit.addEntry(entry);
                        auditRepo.update(audit);
                    }
                    oraRepo.delete(oraApplication);
                    oraApplicationHelper.clearCafDocsFromRecruitmentBucket(oraApplication.getApplicant_urn(),
                            oraApplication.getApplication_info().getRecruitmentCode());
                    eligibilityResponse.setEligibleForUnlock(true);
                    eligibilityResponse.setMessage("CAF Unlocked. Current Step: " + currentStepKey);
                } catch (Exception e) {
                    log.error("CAF unlock failed; skipping delete for URN {} and vacancy {}",
                            oraApplication.getApplicant_urn(),
                            oraApplication.getVacancyId(),
                            e);
                    eligibilityResponse.setEligibleForUnlock(false);
                    eligibilityResponse.setMessage("CAF unlock failed due to an internal error");
                }
        }

        log.info("unlockCaf | status=COMPLETED | currentStepKey={} | response={}", currentStepKey,
                oraApplicationHelper.safeJson(eligibilityResponse));
        return eligibilityResponse;

    }

    @CacheInvalidateAll(cacheName = "ora-applications")
    public String deleteAccount(String urn) {
        log.info("deleteAccount | request={{\"applicant_urn\":\"{}\"}}", urn);
        log.info("Deleting account for URN: {}", urn);

        Long id;
        try {
            id = Long.parseLong(urn);
        } catch (NumberFormatException e) {
            log.error("Invalid URN format: {}", urn);
            String response = "Invalid URN format: " + urn;
            log.info(DELETE_ACC_RESPONSE_LOG, oraApplicationHelper.safeJson(response));
            return response;
        }

        // Check if user exists in UserProfile
        UserProfile userProfile = userRepo.findById(id);
        if (userProfile == null) {
            log.error("User not found for URN: {}", urn);
            String response = "User not found for URN: " + urn;
            log.info(DELETE_ACC_RESPONSE_LOG, oraApplicationHelper.safeJson(response));
            return response;
        }

        // Delete from all repositories
        oraRepo.delete("applicant_urn", urn);
        auditRepo.delete("applicant_urn", urn);
        cafRepo.delete("_id", id);
        userRepo.delete("_id", id);

        log.info("Account deleted successfully for URN: {}", urn);
        String response = "Account deleted successfully for URN: " + urn;
        log.info(DELETE_ACC_RESPONSE_LOG, oraApplicationHelper.safeJson(response));
        return response;
    }

    public List<OraApplicationReportRecord> getReportByRecruitmentCode(String recruitmentCode) {
        return getReportByRecruitmentCode(recruitmentCode, null);
    }

    public List<OraApplicationReportRecord> getReportByRecruitmentCode(String recruitmentCode, Integer preference) {
        return getReportByRecruitmentCode(recruitmentCode, preference, null);
    }

    public List<OraApplicationReportRecord> getReportByRecruitmentCode(String recruitmentCode, Integer preference,
            String centerName) {
        log.info("getReportByRecruitmentCode | recruitmentCode={} | preference={} | centerName={}", recruitmentCode,
                preference, centerName);
        List<OraApplication> applications = oraRepo.findSubmittedByRecruitmentCode(recruitmentCode);
        if (applications == null || applications.isEmpty()) {
            log.info("getReportByRecruitmentCode | status=NO_DATA | recruitmentCode={} | preference={} | centerName={}",
                    recruitmentCode, preference, centerName);
            return new ArrayList<>();
        }

        List<Long> userIds = applications.stream()
                .map(a -> {
                    try {
                        return Long.parseLong(a.getApplicant_urn());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        Map<Long, UserProfile> profileMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            List<UserProfile> profiles = userRepo.find("_id in ?1", userIds).list();
            profileMap = profiles.stream().collect(Collectors.toMap(UserProfile::getId, p -> p, (a, b) -> a));
        }

        Map<Long, UserProfile> finalProfileMap = profileMap;
        List<OraApplicationReportRecord> response = applications.stream()
                .filter(app -> {
                    String center = OraApplicationUtil.normalizeCenter(app, preference);
                    // Agar center fill nahi ki toh skip karo
                    if (center == null || center.trim().isEmpty()) {
                        return false;
                    }
                    // Agar centerName filter diya gaya hai toh sirf usi se match karo
                    // (case-insensitive)
                    if (centerName != null && !centerName.trim().isEmpty()) {
                        return center.trim().equalsIgnoreCase(centerName.trim());
                    }
                    return true;
                })
                .map(app -> {
                    Long userId = null;
                    try {
                        userId = Long.parseLong(app.getApplicant_urn());
                    } catch (Exception e) {
                        // Ignore exception
                    }
                    UserProfile profile = (userId != null) ? finalProfileMap.get(userId) : null;
                    return mapToReportRecord(app, profile, preference);
                })
                .toList();
        log.info(
                "getReportByRecruitmentCode | status=SUCCESS | recruitmentCode={} | preference={} | centerName={} | resultCount={}",
                recruitmentCode, preference, centerName, response.size());
        return response;
    }

    public List<CommonCandidateReportRecord> getCommonCandidates(List<AdvertisementInput> advertisements) {
        log.info("getCommonCandidates | advertisementsCount={}", advertisements != null ? advertisements.size() : 0);
        if (advertisements == null || advertisements.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> allVacancyIds = advertisements.stream()
                .flatMap(ad -> ad.getVacancyNumbers().stream())
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (allVacancyIds.isEmpty()) {
            log.info("getCommonCandidates | status=NO_VACANCIES");
            return new ArrayList<>();
        }

        List<OraApplication> applications = oraRepo.findSubmittedByVacancyIds(allVacancyIds);
        if (applications == null || applications.isEmpty()) {
            log.info("getCommonCandidates | status=NO_APPLICATIONS");
            return new ArrayList<>();
        }

        // Group by URN
        Map<String, List<OraApplication>> groupedByUrn = applications.stream()
                .collect(Collectors.groupingBy(OraApplication::getApplicant_urn));

        // Filter those with > 1 application
        List<String> commonUrns = groupedByUrn.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .toList();

        if (commonUrns.isEmpty()) {
            log.info("getCommonCandidates | status=NO_COMMON_CANDIDATES");
            return new ArrayList<>();
        }

        // Fetch profiles for these URNs
        List<Long> userIds = commonUrns.stream().map(Long::parseLong).toList();
        Map<Long, UserProfile> profileMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            List<UserProfile> profiles = userRepo.find("_id in ?1", userIds).list();
            profileMap = profiles.stream().collect(Collectors.toMap(UserProfile::getId, p -> p, (a, b) -> a));
        }

        List<CommonCandidateReportRecord> response = generateReportRecord(commonUrns, groupedByUrn, profileMap);

        log.info("getCommonCandidates | status=SUCCESS | resultCount={}", response.size());
        return response;
    }

    private List<CommonCandidateReportRecord> generateReportRecord(List<String> commonUrns,
            Map<String, List<OraApplication>> groupedByUrn, Map<Long, UserProfile> profileMap) {
        return commonUrns.stream().map(urn -> {
            List<OraApplication> userApps = groupedByUrn.get(urn);
            UserProfile profile = profileMap.get(Long.parseLong(urn));

            String advs = userApps.stream()
                    .map(a -> a.getApplication_info() != null ? a.getApplication_info().getRecruitmentCode() : "")
                    .distinct()
                    .collect(Collectors.joining(", "));

            String vacs = userApps.stream()
                    .map(OraApplication::getVacancyId)
                    .distinct()
                    .collect(Collectors.joining(", "));

            String appIds = userApps.stream()
                    .map(a -> String.valueOf(a.getApplicationId()))
                    .distinct()
                    .collect(Collectors.joining(", "));

            return CommonCandidateReportRecord.builder()
                    .applicant_urn(urn)
                    .name(profile != null ? profile.getCandidateName() : getCandidateNameFromApp(userApps.get(0)))
                    .mobileNo(profile != null ? String.valueOf(profile.getMobileNo()) : "")
                    .email(profile != null ? profile.getEmail() : "")
                    .advertisementNo(advs)
                    .vacancyNo(vacs)
                    .applicationIds(appIds)
                    .build();
        }).toList();
    }

    public UserProfileResponse updateProfileCompletionStatus(String applicantURN) throws GraphQLException {
        log.info("updateProfileCompletionStatus | request={{\"applicant_urn\":\"{}\"}}", applicantURN);
        Long applicantId = Long.parseLong(applicantURN);
        log.info("Checking profile completion status for URN: {}", applicantURN);

        UserProfile userProfile = userRepo.findById(applicantId);
        if (userProfile == null) {
            log.error("Profile completion update failed. User Profile not found for URN: {}", applicantURN);
            throw new GraphQLException("User Profile not found with URN: " + applicantURN);
        }

        CandidateProfile cafProfile = cafRepo.findById(applicantId);
        if (cafProfile == null) {
            log.error("Profile completion update failed. CAF Details not found for URN: {}", applicantURN);
            throw new GraphQLException("CAF Details not found with URN: " + applicantURN);
        }

        List<String> missingSections = oraApplicationHelper.getMissingCafSections(cafProfile);
        boolean isComplete = true;
        log.info("isComplete value for URN {} = {}", applicantURN, isComplete);
        if (isComplete) {
            userProfile.setIsProfileComplete(true);
            userProfile.setCafSubmitIon(OraApplicationUtil.nowIst());
            userRepo.persistOrUpdate(userProfile);
            log.info("Profile completion updated for URN: {}. isProfileComplete=true, cafSubmitIon={}",
                    applicantURN, userProfile.getCafSubmitIon());
            oraApplicationHelper.sendProfileCompletionNotifications(userProfile, applicantURN);
        } else {
            log.warn("Profile completion not updated for URN: {}. Missing CAF sections: {}",
                    applicantURN, missingSections);
        }

        UserProfileResponse response = oraApplicationHelper.buildUserProfileResponse(userProfile);
        if (!isComplete) {
            response.setIsProfileComplete(false);
        }

        log.info("updateProfileCompletionStatus | response={}", oraApplicationHelper.safeJson(response));
        return response;
    }

    private OraApplicationReportRecord mapToReportRecord(OraApplication app, UserProfile profile, Integer preference) {
        return OraApplicationReportRecord.builder()
                .applicant_urn(app.getApplicant_urn())
                .applicationId(app.getApplicationId())
                .name(profile != null ? profile.getCandidateName() : getCandidateNameFromApp(app))
                .gender(normalizeGender(app))
                .mobileNo(profile != null ? String.valueOf(profile.getMobileNo()) : "")
                .email(profile != null ? profile.getEmail() : "")
                .submittedAt(OraApplicationUtil.getSubmittedAt(app))
                .community(normalizeCommunity(app))
                .paymentStatus(OraApplicationUtil.normalizePaymentStatus(app))
                .paymentMode(app.getPayment_details() != null ? app.getPayment_details().getPayment_mode() : "")
                .amount(app.getPayment_details() != null ? app.getPayment_details().getAmount() : "")
                .transactionId(app.getPayment_details() != null ? app.getPayment_details().getTransaction_id() : "")
                .applicationStatus(app.getApplication_info() != null ? app.getApplication_info().getStatus() : "")
                .center(OraApplicationUtil.normalizeCenter(app, preference))
                .build();
    }

    private String getCandidateNameFromApp(OraApplication app) {
        if (app.getCaf_details() != null && app.getCaf_details().getPreIdentity() != null) {
            PreIdentity pre = app.getCaf_details().getPreIdentity();
            StringBuilder sb = new StringBuilder();
            if (pre.getFirstName() != null)
                sb.append(pre.getFirstName()).append(" ");
            if (pre.getMiddleName() != null)
                sb.append(pre.getMiddleName()).append(" ");
            if (pre.getLastName() != null)
                sb.append(pre.getLastName());
            return sb.toString().trim();
        }
        return "";
    }

    private String normalizeGender(OraApplication app) {
        if (app.getCaf_details() != null && app.getCaf_details().getPreIdentity() != null
                && app.getCaf_details().getPreIdentity().getGenderId() != null) {
            String mapped = masterDataCache.lookup(MasterDataConstants.GENDERS,
                    String.valueOf(app.getCaf_details().getPreIdentity().getGenderId()));
            return mapped != null ? mapped : UNKNOWN;
        }
        return UNKNOWN;
    }

    private String normalizeCommunity(OraApplication app) {
        if (app.getCaf_details() != null && app.getCaf_details().getCommunity() != null
                && app.getCaf_details().getCommunity().getCategoryId() != null) {
            String mapped = masterDataCache.lookup(MasterDataConstants.COMMUNITIES,
                    String.valueOf(app.getCaf_details().getCommunity().getCategoryId()));
            return mapped != null ? mapped : UNKNOWN;
        }
        return UNKNOWN;
    }

    public UserProfileResponse getProfileByUrn(String applicantURN) throws GraphQLException {
        log.info("getProfileByUrn | request={{\"applicant_urn\":\"{}\"}}", applicantURN);

        Long applicantId = Long.parseLong(applicantURN);

        UserProfile userProfile = userRepo.findById(applicantId);
        UserProfileResponse response = oraApplicationHelper.buildUserProfileResponse(userProfile);
        log.info("getProfileByUrn | response={}", oraApplicationHelper.safeJson(response));
        return response;

    }

}
