package in.nic.upscora.graphql.form4.utils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.graphql.GraphQLException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.nic.upscora.graphql.form4.client.NgrpDocumentClient;
import in.nic.upscora.graphql.form4.client.NotificationClient;
import in.nic.upscora.graphql.form4.client.model.ChannelConfig;
import in.nic.upscora.graphql.form4.client.model.Metadata;
import in.nic.upscora.graphql.form4.client.model.NotificationRequest;
import in.nic.upscora.graphql.form4.mongo.entity.application.Experience;
import in.nic.upscora.graphql.form4.mongo.entity.application.OraApplication;
import in.nic.upscora.graphql.form4.mongo.entity.caf.CandidateProfile;
import in.nic.upscora.graphql.form4.mongo.entity.userprofile.UserProfile;
import in.nic.upscora.graphql.form4.mongo.entity.vacancy.Vacancy;
import in.nic.upscora.graphql.form4.mongo.repository.VacancyRepository;
import in.nic.upscora.graphql.form4.response.UserProfileResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class OraApplicationHelper {

    private final NotificationClient notificationClient;
    private final NgrpDocumentClient ngrpDocumentClient;
    private final VacancyRepository vacancyRepo;

    private final ObjectMapper objectMapper;

    private static final DateTimeFormatter EXPERIENCE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String CANDIDATE = "Candidate";
    private static final String EMAIL = "email";

    @Inject
    public OraApplicationHelper(@RestClient NotificationClient notificationClient,
            @RestClient NgrpDocumentClient ngrpDocumentClient, VacancyRepository vacancyRepo,
            ObjectMapper objectMapper) {
        this.notificationClient = notificationClient;
        this.ngrpDocumentClient = ngrpDocumentClient;
        this.vacancyRepo = vacancyRepo;
        this.objectMapper = objectMapper;

    }

    public void copyDocsToExamBucket(String applicantUrn, String postId, CandidateProfile candidateProfile)
            throws GraphQLException {
        if (candidateProfile == null) {
            return;
        }

        if (postId == null || postId.isBlank()) {
            log.warn("Skipping recruitment copy because post_id is missing for applicantUrn={}", applicantUrn);
            return;
        }

        log.info("Recruitment doc copy request applicantUrn={} postId={} endpoint=/ora_api/s3/usertorecruitment",
                applicantUrn,
                postId);
        try (Response response = ngrpDocumentClient.copyUserDocsToRecruitment(applicantUrn, postId)) {
            String body = response.readEntity(String.class);
            log.info("Recruitment doc copy response applicantUrn={} postId={} status={} bodySnippet={}",
                    applicantUrn,
                    postId,
                    response.getStatus(),
                    OraApplicationUtil.abbreviate(body, 300));
            if (response.getStatus() < 200 || response.getStatus() >= 300) {
                throw new GraphQLException(
                        "Failed to copy documents to recruitment bucket for applicant " + applicantUrn
                                + " and post " + postId);
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> result = objectMapper.readValue(body, Map.class);
            Object status = result.get("status");
            if (status instanceof Boolean stat && !stat.booleanValue()) {
                throw new GraphQLException("Document copy API returned status=false for applicant " + applicantUrn
                        + " and post " + postId);
            }
        } catch (GraphQLException e) {
            throw e;
        } catch (Exception e) {
            log.error("Recruitment doc copy exception applicantUrn={} postId={}",
                    applicantUrn,
                    postId,
                    e);
            throw new GraphQLException("Error while copying documents to recruitment bucket", e);
        }
    }

    public String safeJson(Object value) {
        if (value == null) {
            return "null";
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            return String.valueOf(value);
        }
    }

    public void applyExperienceCalculationDate(String vacancyId, List<Experience> experiences) {
        if (experiences == null || experiences.isEmpty() || vacancyId == null || vacancyId.isBlank()) {
            return;
        }

        String calculationDate = resolveExperienceCalculationDate(vacancyId);
        if (calculationDate == null || calculationDate.isBlank()) {
            log.warn("applyExperienceCalculationDate | vacancyId={} | calculationDateMissing=true", vacancyId);
            return;
        }

        for (Experience experience : experiences) {
            if (experience != null && experience.isContinuing()) {
                experience.setDateTo(calculationDate);
            }
        }
    }

    private String resolveExperienceCalculationDate(String vacancyId) {
        Vacancy vacancy = vacancyRepo.findByVacancyId(vacancyId);
        if (vacancy == null || vacancy.getExperienceCalculationDate() == null) {
            return null;
        }
        return vacancy.getExperienceCalculationDate().format(EXPERIENCE_DATE_FORMATTER);
    }

    public void sendProfileCompletionNotifications(UserProfile userProfile, String applicantURN) {
        try {
            String correlationId = "otp-" + applicantURN + "-" + System.currentTimeMillis();

            Map<String, ChannelConfig> channels = new HashMap<>();

            // SMS Channel
            if (userProfile.getMobileNo() != null) {
                String mobile = userProfile.getMobileNo().toString();
                if (!mobile.startsWith("+")) {
                    mobile = "+91" + mobile;
                }
                channels.put("sms", ChannelConfig.builder()
                        .recipient(mobile)
                        .templateName("CAF_SUBMITTED")
                        .variables(Map.of(
                                "name",
                                userProfile.getCandidateName() != null ? userProfile.getCandidateName() : CANDIDATE,
                                "urn", applicantURN))
                        .metadata(Metadata.builder().ttlSeconds(300).build())
                        .build());
            }

            // Email Channel
            if (userProfile.getEmail() != null) {
                channels.put(EMAIL, ChannelConfig.builder()
                        .recipient(userProfile.getEmail())
                        .templateName("CAF_SUBMITTED")
                        .variables(Map.of(
                                "name",
                                userProfile.getCandidateName() != null ? userProfile.getCandidateName() : CANDIDATE,
                                "urn", applicantURN))
                        .metadata(Metadata.builder().ttlSeconds(300).build())
                        .build());
            }

            if (!channels.isEmpty()) {
                NotificationRequest request = NotificationRequest.builder()
                        .eventCode("otp")
                        .channels(channels)
                        .build();

                notificationClient.sendNotification(correlationId, "en", request);
                log.info("Profile completion notifications sent for URN: {}", applicantURN);
            }
        } catch (Exception e) {
            log.error("Failed to send profile completion notifications for URN: {}", applicantURN, e);
        }
    }

    public void clearCafDocsFromRecruitmentBucket(String applicantUrn, String postId) {
        if (applicantUrn == null || applicantUrn.isBlank() || postId == null || postId.isBlank()) {
            log.warn("Skipping recruitment bucket clear due to missing identifiers applicantUrn={} postId={}",
                    applicantUrn,
                    postId);
            return;
        }

        log.info("Recruitment bucket clear request applicantUrn={} postId={} endpoint=/ora_api/s3/clearcaf",
                applicantUrn,
                postId);
        try (Response response = ngrpDocumentClient.clearCafDocsFromRecruitment(applicantUrn, postId)) {
            String body = response.readEntity(String.class);
            log.info("Recruitment bucket clear response applicantUrn={} postId={} status={} bodySnippet={}",
                    applicantUrn,
                    postId,
                    response.getStatus(),
                    OraApplicationUtil.abbreviate(body, 300));
            if (response.getStatus() < 200 || response.getStatus() >= 300) {
                log.warn("Recruitment bucket clear failed applicantUrn={} postId={} status={} bodySnippet={}",
                        applicantUrn,
                        postId,
                        response.getStatus(),
                        OraApplicationUtil.abbreviate(body, 300));
            }
        } catch (Exception e) {
            log.warn("Recruitment bucket clear exception applicantUrn={} postId={}",
                    applicantUrn,
                    postId,
                    e);
        }
    }

    public void sendCafLockedNotifications(UserProfile userProfile, OraApplication oraApplication) {
        try {
            String applicantURN = oraApplication.getApplicant_urn();
            String correlationId = "caf-" + applicantURN + "-" + System.currentTimeMillis();

            Map<String, ChannelConfig> channels = new HashMap<>();

            Map<String, String> variables = Map.of(
                    "name", userProfile.getCandidateName() != null ? userProfile.getCandidateName() : CANDIDATE,
                    "urn", applicantURN,
                    "exam", oraApplication.getApplication_info().getPost_name(),
                    "year", oraApplication.getApplication_info().getRecruitmentYear());

            // SMS Channel
            if (userProfile.getMobileNo() != null) {
                String mobile = userProfile.getMobileNo().toString();
                if (!mobile.startsWith("+")) {
                    mobile = "+91" + mobile;
                }
                channels.put("sms", ChannelConfig.builder()
                        .recipient(mobile)
                        .templateName("CAF_LOCKED")
                        .variables(variables)
                        .metadata(Metadata.builder().ttlSeconds(300).build())
                        .build());
            }

            // Email Channel
            if (userProfile.getEmail() != null) {
                channels.put(EMAIL, ChannelConfig.builder()
                        .recipient(userProfile.getEmail())
                        .templateName("CAF_LOCKED")
                        .variables(variables)
                        .metadata(Metadata.builder().ttlSeconds(300).build())
                        .build());
            }

            if (!channels.isEmpty()) {
                NotificationRequest request = NotificationRequest.builder()
                        .eventCode("otp")
                        .channels(channels)
                        .build();

                notificationClient.sendNotification(correlationId, "en", request);
                log.info("CAF Locked notifications sent for URN: {}", applicantURN);
            }
        } catch (Exception e) {
            log.error("Failed to send CAF Locked notifications for URN: {}", oraApplication.getApplicant_urn(), e);
        }
    }

    public UserProfileResponse buildUserProfileResponse(UserProfile userProfile) throws GraphQLException {
        if (userProfile == null) {
            throw new GraphQLException("User Profile not found");
        }

        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setEmail(userProfile.getEmail());
        userProfileResponse
                .setMobileNum(userProfile.getMobileNo() != null ? userProfile.getMobileNo().toString() : null);
        userProfileResponse.setIsProfileComplete(userProfile.getIsProfileComplete());
        userProfileResponse.setCafSubmitIon(userProfile.getCafSubmitIon());
        return userProfileResponse;
    }

    public List<String> getMissingCafSections(CandidateProfile cafProfile) {
        List<String> missingSections = new ArrayList<>();

        if (cafProfile.getPreIdentity() == null)
            missingSections.add("preIdentity");
        // if (cafProfile.getDocs() == null) missingSections.add("docs");
        if (cafProfile.getIdentity() == null)
            missingSections.add("identity");
        if (cafProfile.getUidData() == null && cafProfile.getPidData() == null) {
            missingSections.add("uidDataOrPidData");
        }
        if (cafProfile.getPersonalProfile() == null)
            missingSections.add("personalProfile");
        if (cafProfile.getParents() == null)
            missingSections.add("parents");
        if (cafProfile.getCommunity() == null)
            missingSections.add("community");
        if (cafProfile.getDisability() == null)
            missingSections.add("disability");
        if (cafProfile.getAddress() == null)
            missingSections.add("address");
        if (cafProfile.getQualifications() == null)
            missingSections.add("qualifications");
        if (cafProfile.getEmployment() == null)
            missingSections.add("employment");
        if (cafProfile.getAchievements() == null)
            missingSections.add("achievements");
        if (cafProfile.getExams() == null)
            missingSections.add("exams");

        return missingSections;
    }

    public void sendFinalSubmissionNotifications(UserProfile userProfile, OraApplication oraApplication) {
        try {
            String applicantURN = oraApplication.getApplicant_urn();
            String correlationId = "otp-" + applicantURN + "-" + System.currentTimeMillis();

            Map<String, ChannelConfig> channels = new HashMap<>();

            Map<String, String> variables = Map.of(
                    "name", userProfile.getCandidateName() != null ? userProfile.getCandidateName() : CANDIDATE,
                    "urn", applicantURN,
                    "appno", oraApplication.getApplicationId().toString(),
                    "exam", oraApplication.getApplication_info().getPost_name(),
                    "year", oraApplication.getApplication_info().getRecruitmentYear());

            // SMS Channel
            if (userProfile.getMobileNo() != null) {
                String mobile = userProfile.getMobileNo().toString();
                if (!mobile.startsWith("+")) {
                    mobile = "+91" + mobile;
                }
                channels.put("sms", ChannelConfig.builder()
                        .recipient(mobile)
                        .templateName("FINAL_SUBMISSION")
                        .variables(variables)
                        .metadata(Metadata.builder().ttlSeconds(300).build())
                        .build());
            }

            // Email Channel
            if (userProfile.getEmail() != null) {
                channels.put(EMAIL, ChannelConfig.builder()
                        .recipient(userProfile.getEmail())
                        .templateName("FINAL_SUBMISSION")
                        .variables(variables)
                        .metadata(Metadata.builder().ttlSeconds(300).build())
                        .build());
            }

            if (!channels.isEmpty()) {
                NotificationRequest request = NotificationRequest.builder()
                        .eventCode("otp")
                        .channels(channels)
                        .build();

                notificationClient.sendNotification(correlationId, "en", request);
                log.info("Final submission notifications sent for URN: {}", applicantURN);
            }
        } catch (Exception e) {
            log.error("Failed to send final submission notifications for URN: {}", oraApplication.getApplicant_urn(),
                    e);
        }
    }

}
