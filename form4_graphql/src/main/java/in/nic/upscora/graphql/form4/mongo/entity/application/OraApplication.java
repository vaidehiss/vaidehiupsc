package in.nic.upscora.graphql.form4.mongo.entity.application;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.graphql.Name;

import in.nic.upscora.graphql.form4.mongo.entity.application.info.OraApplicationInfo;
import in.nic.upscora.graphql.form4.mongo.entity.caf.CandidateProfile;
import in.nic.upscora.graphql.form4.request.AllRequestCommonInfo;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity(collection = "ora_Exam1", clientName = "oradb")
public class OraApplication {



    @BsonId
    @BsonProperty("_id")
    private ObjectId id;

    @NotBlank
    @NotEmpty
    @NotNull
    private String applicant_urn;
    
    @NotBlank
    @NotEmpty
    @NotNull
    private Long applicationId;

    @NotBlank
    @NotEmpty
    @NotNull
    private String vacancyId;

    
    private OraApplicationInfo application_info;
    private CandidateProfile caf_details;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String createdAtIst;
    private String updatedAtIst;

    private String claimAgeRelaxation;
    private String relaxationCategoryCode;
    private String supportDocument;
    private String biggerFontSize;
    private String compensatoryTime;
    private String wantScribe;
    private Boolean testing;
    private String assistiveDevice;
    private AssistiveDeviceType assistiveDevices;

    private List<OraQualification> essential_qualification;
    private List<Experience> essential_experience;
    
    private String desirableQualificationApplicable;
    private List<OraQualification> desirable_qualification;

    private String desirableExperienceApplicable;
    private List<Experience> desirable_experience;

    private String additionalQualificationApplicable;
    private List<OraQualification> additional_qualification;

    private String additionalExperienceApplicable;
    private List<Experience> additional_experience;

    private List<NetQualification> netQualifications;

    private List<GateQualification> gateQualifications;

    private String diplomaApplicable;
    private List<DiplomaDetails> diplomas;

    private List<CertificateDetails> certificates;

    private List<InternshipDetails> internships;

    private List<PublicationDetails> publications;

    private List<ConferenceDetails> conferences;

    private List<Registration> registrations;

    private List<LanguageDetails> languages;

    private List<QuestionAnswer> answers;

    private ExamDocs examDocs;

    private String otherDocsApplicable;
    private String mergedDocument;

    //CentrePreference
    private List<String> centerPreferences;

    @Name("paymentDetails")
    @BsonProperty("payment_details")
    private PaymentDetails payment_details;

    //To be removed Later
    private String status;

    private String submittedAt;
    private String submittedAtIst;

    private boolean submitDeclarationAccepted;

    private boolean centerDeclarationAccepted;




    public void setValues(AllRequestCommonInfo application_info, Long applicationId, CandidateProfile caf_details) {
        this.setApplicant_urn(application_info.getApplicant_urn());
        this.setApplicationId(applicationId);
        this.setVacancyId(application_info.getVacancyId());
        this.setApplication_info(new OraApplicationInfo(application_info));
        this.setCaf_details(caf_details);
        this.getApplication_info().setCafLocked(true);
     
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
    }





    
}
