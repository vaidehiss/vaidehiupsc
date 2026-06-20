package in.nic.upscora.graphql.form4.resource;

import org.eclipse.microprofile.graphql.*;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.nic.upscora.graphql.form4.mongo.entity.application.OraApplication;
import in.nic.upscora.graphql.form4.mongo.entity.application.OraExam1Report;
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
import in.nic.upscora.graphql.form4.service.OraApplicationService;
import in.nic.upscora.graphql.form4.service.OraExam1ReportService;

import io.smallrye.mutiny.Multi;
import io.smallrye.graphql.api.Subscription;
import io.smallrye.common.annotation.Blocking;

import io.vertx.ext.web.RoutingContext;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@GraphQLApi
public class OraApplicationGraphQLResource {

        private final OraApplicationService oraService;
        private final OraExam1ReportService reportService;
        private final ObjectMapper objectMapper;
        private final RoutingContext routingContext;

        public OraApplicationGraphQLResource(OraApplicationService oraService, OraExam1ReportService reportService,
                        ObjectMapper objectMapper, RoutingContext routingContext) {
                this.oraService = oraService;
                this.reportService = reportService;
                this.objectMapper = objectMapper;
                this.routingContext = routingContext;
        }

        @Timed(value = "getAllApplications_timer",

                        description = "Time taken to fetch all applications")
        @Counted(value = "getAllApplications_count", description = "Number of times all applications were fetched")
        @Query("allApplications")
        public List<OraApplication> getAllApplications() {
                log.info("allApplications | request=fetch-all");
                List<OraApplication> response = oraService.getAllApplicants();
                log.info("allApplications | resultCount={} | response={}", response.size(), safeJson(response));
                return response;

        }

        @Query("genderWiseBreakup")
        public List<OraApplicationReportRecord> getGenderWiseBreakup(
                        @Name("recruitmentCode") @NonNull String recruitmentCode) {
                log.info("genderWiseBreakup | recruitmentCode={}", recruitmentCode);
                return oraService.getReportByRecruitmentCode(recruitmentCode);
        }

        @Query("communityWiseBreakup")
        public List<OraApplicationReportRecord> getCommunityWiseBreakup(
                        @Name("recruitmentCode") @NonNull String recruitmentCode) {
                log.info("communityWiseBreakup | recruitmentCode={}", recruitmentCode);
                return oraService.getReportByRecruitmentCode(recruitmentCode);
        }

        @Query("communityGenderWiseBreakup")
        public List<OraApplicationReportRecord> getCommunityGenderWiseBreakup(
                        @Name("recruitmentCode") @NonNull String recruitmentCode) {
                log.info("communityGenderWiseBreakup | recruitmentCode={}", recruitmentCode);
                return oraService.getReportByRecruitmentCode(recruitmentCode);
        }

        @Query("paymentWiseBreakup")
        public List<OraApplicationReportRecord> getPaymentWiseBreakup(
                        @Name("recruitmentCode") @NonNull String recruitmentCode) {
                log.info("paymentWiseBreakup | recruitmentCode={}", recruitmentCode);
                return oraService.getReportByRecruitmentCode(recruitmentCode);
        }

        @Query("applicationStatusWiseBreakup")
        public List<OraApplicationReportRecord> getApplicationStatusWiseBreakup(
                        @Name("recruitmentCode") @NonNull String recruitmentCode) {
                log.info("applicationStatusWiseBreakup | recruitmentCode={}", recruitmentCode);
                return oraService.getReportByRecruitmentCode(recruitmentCode);
        }

        @Query("centerWiseBreakup")
        public List<OraApplicationReportRecord> getCenterWiseBreakup(
                        @Name("recruitmentCode") @NonNull String recruitmentCode,
                        @Name("preference") Integer preference,
                        @Name("centerName") String centerName) {
                log.info("centerWiseBreakup | recruitmentCode={} | preference={} | centerName={}", recruitmentCode,
                                preference, centerName);
                return oraService.getReportByRecruitmentCode(recruitmentCode, preference, centerName);
        }

        @Query("commonCandidates")
        public List<CommonCandidateReportRecord> getCommonCandidates(
                        @Name("advertisements") List<AdvertisementInput> advertisements) {
                log.info("commonCandidates | advertisementsCount={}",
                                advertisements != null ? advertisements.size() : 0);
                return oraService.getCommonCandidates(advertisements);
        }

        @Timed(value = "getApplicationByUrn_timer",

                        description = "Time taken to fetch application by URN")
        @Counted(value = "getApplicationByUrn_count", description = "Number of times application was fetched by URN")
        @Query("getApplicationByUrn")
        public OraApplication getApplicationByUrn(
                        @Name("applicantInfo") Ora_Applicant applicantInfo) {
                log.info("getApplicationByUrn | request={}", safeJson(applicantInfo));

                String applicantURN = applicantInfo.getApplicant_info().getApplicant_urn();
                String vacancyId = applicantInfo.getApplicant_info().getVacancyId();
                log.info("getApplicationByUrn | lookup applicantUrn={} | vacancyId={}", applicantURN, vacancyId);
                OraApplication response = oraService.getApplicationByUrnAndVacany(applicantURN, vacancyId);
                log.info("getApplicationByUrn | response={}", safeJson(response));
                return response;
        }

        @Timed(value = "getProfileLocked_timer",

                        description = "Time taken to fetch profile lock status")
        @Counted(value = "getProfileLocked_count", description = "Number of times profile lock status was fetched")
        @Query("getProfileLocked")
        public boolean getProfileLocked(
                        @Name("applicantInfo") Ora_Applicant applicantInfo) {
                log.info("getProfileLocked | request={}", safeJson(applicantInfo));
                boolean response = oraService.getProfileLocked(applicantInfo);
                log.info("getProfileLocked | response={}", response);
                return response;
        }

        @Timed(value = "getAllSubmittedExams_timer",

                        description = "Time taken to fetch all submitted exams by URN")
        @Counted(value = "getAllSubmittedExams_count", description = "Number of times submitted exams were fetched by URN")
        @Query("getAllSubmittedExams")
        public List<OraApplication> getAllSubmittedExams(
                        @Name("applicant_urn") @NonNull String applicantURN) {
                log.info("getAllSubmittedExams | request={{\"applicant_urn\":\"{}\"}}", applicantURN);
                List<OraApplication> response = oraService.getAllSubmittedExamsByUrn(applicantURN);
                log.info("getAllSubmittedExams | resultCount={} | response={}", response.size(), safeJson(response));
                return response;
        }

        @Query("oraExam1Report")
        @Blocking
        public OraExam1Report getOraExam1Report(@Name("vacancyId") @NonNull String vacancyId) {
                log.info("oraExam1Report | request={{\"vacancyId\":\"{}\"}}", vacancyId);
                OraExam1Report response = reportService.getOrBuildReport(vacancyId);
                log.info("oraExam1Report | response={}", safeJson(response));
                return response;
        }

        @Subscription("oraExam1ReportUpdates")
        @Blocking
        public Multi<OraExam1Report> oraExam1ReportUpdates(@Name("vacancyId") @NonNull String vacancyId) {
                log.info("oraExam1ReportUpdates | request={{\"vacancyId\":\"{}\"}}", vacancyId);
                return reportService.subscribe(vacancyId);
        }

        @Mutation("invalidateOraExam1ReportCache")
        public boolean invalidateOraExam1ReportCache(@Name("vacancyId") @NonNull String vacancyId) {
                log.info("invalidateOraExam1ReportCache | request={{\"vacancyId\":\"{}\"}}", vacancyId);
                reportService.invalidateReportCache(vacancyId);
                return true;
        }

        @Mutation("refreshOraExam1Report")
        public OraExam1Report refreshOraExam1Report(@Name("vacancyId") @NonNull String vacancyId) {
                log.info("refreshOraExam1Report | request={{\"vacancyId\":\"{}\"}}", vacancyId);
                OraExam1Report response = reportService.refreshForVacancy(vacancyId);
                log.info("refreshOraExam1Report | response={}", safeJson(response));
                return response;
        }

        @Timed(value = "getUserProfileByUrn_timer",

                        description = "Time taken to fetch user profile by URN")
        @Counted(value = "getUserProfileByUrn_count", description = "Number of times user profile was fetched by URN")
        @Query("UserDetailsByUrn")
        public UserProfileResponse getUserProfileByUrn(
                        @Name("applicantURN") @NonNull String applicantURN) throws GraphQLException {
                log.info("UserDetailsByUrn | request={{\"applicantURN\":\"{}\"}}", applicantURN);
                UserProfileResponse response = oraService.getProfileByUrn(applicantURN);
                log.info("UserDetailsByUrn | response={}", safeJson(response));
                return response;
        }

        @Timed(value = "updateUserProfileCompletionStatus_timer",

                        description = "Time taken to update user profile completion status")
        @Counted(value = "updateUserProfileCompletionStatus_count", description = "Number of times profile completion status was updated")
        @Query("updateUserProfileCompletionStatus")
        public UserProfileResponse updateUserProfileCompletionStatus(
                        @Name("applicantURN") @NonNull String applicantURN) throws GraphQLException {
                log.info("updateUserProfileCompletionStatus | request={{\"applicantURN\":\"{}\"}}", applicantURN);
                UserProfileResponse response = oraService.updateProfileCompletionStatus(applicantURN);
                log.info("updateUserProfileCompletionStatus | response={}", safeJson(response));
                return response;
        }

        @Timed(value = "getPaymentDetails_timer",

                        description = "Time taken to fetch payment details")
        @Counted(value = "getPaymentDetails_count", description = "Number of times payment details were fetched")
        @Query("getPaymentDetails")
        public List<PaymentDetailsResponse> getPaymentDetails(
                        @Name("applicant_urn") @NonNull String applicantURN,
                        @Name("recruitmentCode") @NonNull String recruitmentCode) {
                log.info("getPaymentDetails | request={{\"applicant_urn\":\"{}\",\"recruitmentCode\":\"{}\"}}",
                                applicantURN, recruitmentCode);
                List<PaymentDetailsResponse> response = oraService
                                .getPaymentDetailsByUrnAndRecruitmentCode(applicantURN, recruitmentCode);
                log.info("getPaymentDetails | resultCount={} | response={}", response.size(), safeJson(response));
                return response;
        }

        @Timed(value = "createOraApplication_timer",

                        description = "Time taken to create a new application")
        @Counted(value = "createOraApplication_count", description = "Number of new applications created")
        @Mutation("addApplicant")
        public OraApplication createOraApplication(
                        @NonNull @Name("applicantInfo") Ora_Applicant applicantInfo) throws GraphQLException {
                log.info("addApplicant | request={}", safeJson(applicantInfo));
                boolean testing = isTestingRequest();
                log.info("addApplicant | x-user-role={} | testing={}", getUserRoleHeader(), testing);
                OraApplication response = oraService.createApplication(applicantInfo, testing);
                log.info("addApplicant | response={}", safeJson(response));
                return response;

        }

        private boolean isTestingRequest() {
                String userRole = getUserRoleHeader();
                return userRole != null && "testing".equalsIgnoreCase(userRole.trim());
        }

        private String getUserRoleHeader() {
                return routingContext != null && routingContext.request() != null
                                ? routingContext.request().getHeader("x-user-role")
                                : null;
        }

        @Timed(value = "unlockCaf_timer",

                        description = "Time taken to check CAF unlock eligibility")
        @Counted(value = "unlockCaf_count", description = "Number of times CAF unlock eligibility was checked")
        @Query("unlockCaf")
        public EligibilityResponse unlockCaf(
                        @NonNull @Name("applicantInfo") Ora_Applicant applicantInfo) {
                log.info("unlockCaf | request={}", safeJson(applicantInfo));
                EligibilityResponse response = oraService.checkCafUnlockEligibility(applicantInfo);
                log.info("unlockCaf | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantAgeRelaxation_timer",

                        description = "Time taken to update age relaxation")
        @Counted(value = "updateApplicantAgeRelaxation_count", description = "Number of age relaxation updates")
        @Mutation("updateApplicantAgeRelaxation")
        public OraApplication updateApplicantAgeRelaxation(
                        @NonNull @Name("applicantInfo") AgeRelaxation applicantInfo) {
                log.info("updateApplicantAgeRelaxation | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantAgeRelaxation(applicantInfo);
                log.info("updateApplicantAgeRelaxation | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantEssentialQualification_timer",

                        description = "Time taken to update essential qualification")
        @Counted(value = "updateApplicantEssentialQualification_count", description = "Number of essential qualification updates")
        @Mutation("updateApplicantEssentialQualification")
        public OraApplication updateApplicantEssentialQualification(
                        @NonNull @Name("applicantInfo") EssentialQualification applicantInfo) {
                log.info("updateApplicantEssentialQualification | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantEssentialQualification(applicantInfo);
                log.info("updateApplicantEssentialQualification | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantEssentialExperience_timer",

                        description = "Time taken to update essential experience")
        @Counted(value = "updateApplicantEssentialExperience_count", description = "Number of essential experience updates")
        @Mutation("updateApplicantEssentialExperience")
        public OraApplication updateApplicantEssentialExperience(
                        @NonNull @Name("applicantInfo") EssentialExperience applicantInfo) {
                log.info("updateApplicantEssentialExperience | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantEssentialExperience(applicantInfo);
                log.info("updateApplicantEssentialExperience | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantDesirableQualification_timer",

                        description = "Time taken to update desirable qualification")
        @Counted(value = "updateApplicantDesirableQualification_count", description = "Number of desirable qualification updates")
        @Mutation("updateApplicantDesirableQualification")
        public OraApplication updateApplicantDesirableQualification(
                        @NonNull @Name("applicantInfo") DesirableQualification applicantInfo) {
                log.info("updateApplicantDesirableQualification | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantDesirableQualification(applicantInfo);
                log.info("updateApplicantDesirableQualification | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantDesirableExperience_timer",

                        description = "Time taken to update desirable experience")
        @Counted(value = "updateApplicantDesirableExperience_count", description = "Number of desirable experience updates")

        @Mutation("updateApplicantDesirableExperience")
        public OraApplication updateApplicantDesirableExperience(
                        @NonNull @Name("applicantInfo") DesirableExperience applicantInfo) {
                log.info("updateApplicantDesirableExperience | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantDesirableExperience(applicantInfo);
                log.info("updateApplicantDesirableExperience | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantAdditionalQualification_timer",

                        description = "Time taken to update additional qualification")
        @Counted(value = "updateApplicantAdditionalQualification_count", description = "Number of additional qualification updates")
        @Mutation("updateApplicantAdditionalQualification")
        public OraApplication updateApplicantAdditionalQualification(
                        @NonNull @Name("applicantInfo") AdditionalQualification applicantInfo) {
                log.info("updateApplicantAdditionalQualification | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantAdditionalQualification(applicantInfo);
                log.info("updateApplicantAdditionalQualification | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantAdditionalExperience_timer",

                        description = "Time taken to update additional experience")
        @Counted(value = "updateApplicantAdditionalExperience_count", description = "Number of additional experience updates")
        @Mutation("updateApplicantAdditionalExperience")
        public OraApplication updateApplicantAdditionalExperience(
                        @NonNull @Name("applicantInfo") AdditionalExperience applicantInfo) {
                log.info("updateApplicantAdditionalExperience | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantAdditionalExperience(applicantInfo);
                log.info("updateApplicantAdditionalExperience | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantNet_timer",

                        description = "Time taken to update NET qualification")
        @Counted(value = "updateApplicantNet_count", description = "Number of NET qualification updates")
        @Mutation("updateApplicantNet")
        public OraApplication updateApplicantNet(
                        @NonNull @Name("applicantInfo") Net applicantInfo) {
                log.info("updateApplicantNet | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantNet(applicantInfo);
                log.info("updateApplicantNet | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantGate_timer",

                        description = "Time taken to update GATE qualification")
        @Counted(value = "updateApplicantGate_count", description = "Number of GATE qualification updates")
        @Mutation("updateApplicantGate")
        public OraApplication updateApplicantGate(
                        @NonNull @Name("applicantInfo") Gate applicantInfo) {
                log.info("updateApplicantGate | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantGate(applicantInfo);
                log.info("updateApplicantGate | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantDiploma_timer",

                        description = "Time taken to update diploma details")
        @Counted(value = "updateApplicantDiploma_count", description = "Number of diploma updates")
        @Mutation("updateApplicantDiploma")
        public OraApplication updateApplicantDiploma(
                        @NonNull @Name("applicantInfo") Diploma applicantInfo) {
                log.info("updateApplicantDiploma | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantDiploma(applicantInfo);
                log.info("updateApplicantDiploma | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantCertificate_timer",

                        description = "Time taken to update certificate details")
        @Counted(value = "updateApplicantCertificate_count", description = "Number of certificate updates")
        @Mutation("updateApplicantCertificate")
        public OraApplication updateApplicantCertificate(
                        @NonNull @Name("applicantInfo") CertificateInput applicantInfo) {
                log.info("updateApplicantCertificate | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantCertificate(applicantInfo);
                log.info("updateApplicantCertificate | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantInternship_timer",

                        description = "Time taken to update internship details")
        @Counted(value = "updateApplicantInternship_count", description = "Number of internship updates")
        @Mutation("updateApplicantInternship")
        public OraApplication updateApplicantInternship(
                        @NonNull @Name("applicantInfo") Internship applicantInfo) {
                log.info("updateApplicantInternship | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantInternship(applicantInfo);
                log.info("updateApplicantInternship | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantPublication_timer",

                        description = "Time taken to update publication details")
        @Counted(value = "updateApplicantPublication_count", description = "Number of publication updates")
        @Mutation("updateApplicantPublication")
        public OraApplication updateApplicantPublication(
                        @NonNull @Name("applicantInfo") Publication applicantInfo) {
                log.info("updateApplicantPublication | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantPublication(applicantInfo);
                log.info("updateApplicantPublication | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantConference_timer",

                        description = "Time taken to update conference details")
        @Counted(value = "updateApplicantConference_count", description = "Number of conference updates")
        @Mutation("updateApplicantConference")
        public OraApplication updateApplicantConference(
                        @NonNull @Name("applicantInfo") Conference applicantInfo) {
                log.info("updateApplicantConference | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantConference(applicantInfo);
                log.info("updateApplicantConference | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantProfessionalReg_timer",

                        description = "Time taken to update professional registration")
        @Counted(value = "updateApplicantProfessionalReg_count", description = "Number of professional registration updates")
        @Mutation("updateApplicantProfessionalReg")
        public OraApplication updateApplicantProfessionalReg(
                        @NonNull @Name("applicantInfo") ProfessionalReg applicantInfo) {
                log.info("updateApplicantProfessionalReg | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantProfessionalReg(applicantInfo);
                log.info("updateApplicantProfessionalReg | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantLanguage_timer",

                        description = "Time taken to update language details")
        @Counted(value = "updateApplicantLanguage_count", description = "Number of language updates")
        @Mutation("updateApplicantLanguage")
        public OraApplication updateApplicantLanguage(
                        @NonNull @Name("applicantInfo") Language applicantInfo) {
                log.info("updateApplicantLanguage | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantLanguage(applicantInfo);
                log.info("updateApplicantLanguage | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantMiscellaneous_timer",

                        description = "Time taken to update miscellaneous details")
        @Counted(value = "updateApplicantMiscellaneous_count", description = "Number of miscellaneous updates")
        @Mutation("updateApplicantMiscellaneous")
        public OraApplication updateApplicantMiscellaneous(
                        @NonNull @Name("applicantInfo") Miscellaneous applicantInfo) {
                log.info("updateApplicantMiscellaneous | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantMiscellaneous(applicantInfo);
                log.info("updateApplicantMiscellaneous | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantOtherDocs_timer",

                        description = "Time taken to update other documents")
        @Counted(value = "updateApplicantOtherDocs_count", description = "Number of other docs updates")
        @Mutation("updateApplicantOtherDocs")
        public OraApplication updateApplicantOtherDocs(
                        @NonNull @Name("applicantInfo") OtherDocs applicantInfo) {
                log.info("updateApplicantOtherDocs | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantOtherDocs(applicantInfo);
                log.info("updateApplicantOtherDocs | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantExamCenter_timer",

                        description = "Time taken to update exam center preferences")
        @Counted(value = "updateApplicantExamCenter_count", description = "Number of exam center updates")
        @Mutation("updateApplicantExamCenter")
        public OraApplication updateApplicantExamCenter(
                        @NonNull @Name("applicantInfo") ExamCenter applicantInfo) {
                log.info("updateApplicantExamCenter | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantExamCenter(applicantInfo);
                log.info("updateApplicantExamCenter | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantProfileLock_timer",

                        description = "Time taken to update profile lock")
        @Counted(value = "updateApplicantProfileLock_count", description = "Number of profile lock updates")
        @Mutation("updateApplicantProfileLock")
        public OraApplication updateApplicantProfileLock(
                        @NonNull @Name("applicantInfo") ProfileLock applicantInfo) {
                log.info("updateApplicantProfileLock | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantProfileLock(applicantInfo);
                log.info("updateApplicantProfileLock | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "unlockProfile_timer",

                        description = "Time taken to unlock profile")
        @Counted(value = "unlockProfile_count", description = "Number of profile unlocks")
        @Mutation("unlockProfile")
        public OraApplication unlockProfile(
                        @NonNull @Name("applicantInfo") ProfileLock applicantInfo) {
                log.info("unlockProfile | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.unlockProfile(applicantInfo);
                log.info("unlockProfile | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "updateApplicantPayment_timer",

                        description = "Time taken to update payment details")
        @Counted(value = "updateApplicantPayment_count", description = "Number of payment updates")
        @Mutation("updateApplicantPayment")
        public OraApplication updateApplicantPayment(
                        @NonNull @Name("applicantInfo") PaymentInput applicantInfo) {
                log.info("updateApplicantPayment | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.updateApplicantPayment(applicantInfo);
                log.info("updateApplicantPayment | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "submitApplication_timer",

                        description = "Time taken to submit an application")
        @Counted(value = "submitApplication_count", description = "Total number of successfully submitted applications")
        @Mutation("submitApplication")
        public OraApplication submitApplication(
                        @NonNull @Name("applicantInfo") SubmitApplication applicantInfo) {
                log.info("submitApplication | request={}", safeJson(applicantInfo));
                OraApplication response = oraService.submitApplication(applicantInfo);
                log.info("submitApplication | response={}", safeJson(response));
                return response;

        }

        @Timed(value = "deleteAccount_timer",

                        description = "Time taken to delete an account")
        @Counted(value = "deleteAccount_count", description = "Number of accounts deleted")
        @Mutation("deleteAccount")
        public String deleteAccount(
                        @NonNull @Name("applicant_urn") String applicantURN) {
                log.info("deleteAccount | request={{\"applicant_urn\":\"{}\"}}", applicantURN);
                String response = oraService.deleteAccount(applicantURN);
                log.info("deleteAccount | response={}", safeJson(response));
                return response;

        }

        private String safeJson(Object value) {
                if (value == null) {
                        return "null";
                }
                try {
                        return objectMapper.writeValueAsString(value);
                } catch (Exception e) {
                        return String.valueOf(value);
                }
        }

}
