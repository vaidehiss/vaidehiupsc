package in.nic.upscora.graphql.form4.mongo.entity.caf;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity(collection = "registeredUsers",clientName = "cafdb")
public class CandidateProfile {

    @BsonId
    @BsonProperty("_id")
    private Long id;

    @BsonProperty("preidentity")
    private PreIdentity preIdentity;

    @BsonProperty("i_on")
    private LocalDateTime createdOn;

    @BsonProperty("is_preIdentity")
    private Boolean isPreIdentity;

    @BsonProperty("docs")
    private Docs docs;

    @BsonProperty("digidocs")
    private Digidocs digidocs;

    @BsonProperty("identity")
    private Identity identity;

    @BsonProperty("uid_data")
    private UidData uidData;

    @BsonProperty("pid_card")
    private PidData pidData; // ✅ fixed duplicate name bug

    @BsonProperty("IdentityProfileStatus")
    private Boolean identityProfileStatus;
    private String board_validation;
    private LocalDateTime urn_lock_ion;
    private LocalDateTime urn_unlock_ist;
    private LocalDateTime urn_relock_ion;
    private Boolean urn_one_time_edit;
    private Boolean preidentity_update;
    private Boolean uid_pid_update;
    private Boolean tenth_update;

    @BsonProperty("personal_profile")
    private PersonalProfile personalProfile;

    private Parents parents;
    private Community community;
    private Disability disability;
    private Address address;

    private Boolean isQualifications;
    private Qualifications qualifications;

    private Employment employment;

    private Boolean isEmployedProfile;

    private Achievements achievements;

    private Boolean isAchievements;

    private Exams exams;

    private Boolean isPrevExam;

    @BsonProperty("examApplied")
    private List<ExamApplied> examApplied;

    @BsonProperty("is_live_uploaded_matched")
    private Boolean isLiveUploadedMatched;

    @BsonProperty("negd_live_vs_uploaded_verification")
    private NegdLiveVsUploadedVerification negdLiveVsUploadedVerification;

    @BsonProperty("is_live_aadhaar_matched")
    private Boolean isLiveAadhaarMatched;

    @BsonProperty("negd_live_vs_aadhaar_verification")
    private NegdLiveVsUploadedVerification negdLiveVsAadhaarVerification;

    @BsonProperty("triple_signature_verified")
    private Boolean tripleSignatureVerified;
}
