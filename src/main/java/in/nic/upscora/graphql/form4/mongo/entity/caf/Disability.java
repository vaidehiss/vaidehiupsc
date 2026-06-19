package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Disability {

    @BsonProperty("is_")
    private Integer isStatus;

    @BsonProperty("is_dis")
    private Boolean isDisabled;

    @BsonProperty("pwd")
    private Pwbd pwd;

    private Pwbd pwbd;

    private Boolean pwbdRecuirmentStatus;

    @BsonProperty("pwbdRecuirment_exam")
    private List<PwbdRequirementExam> pwbdRecuirmentExam;

    @BsonProperty("exam_recm")
    private Boolean examRecm;

    private String examRecuirmentDetails;

    @BsonProperty("i_on")
    private LocalDateTime createdOn;

    @BsonProperty("updatedOn")
    private LocalDateTime updatedOn;

    @BsonIgnore
    private LocalDateTime updatedOnUTC;

    /* =====================================================
       PWBD
       ===================================================== */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pwbd {

        private Integer percentage;

        private String certificateNo;

        @BsonProperty("certificateNo_issuedOn")
        private String certificateNoIssuedOn;

        private String issuingAuthorityDesignation;

        private Integer issuingAuthorityStateId;

        @BsonProperty("address_issuing_authority")
        private String address_issuing_authority;

        private String authorityAddress;

        private Integer disablityTypeId;

        @BsonProperty("tD")
        private List<DisabilityType> td;

        private Boolean writingLimitations;

        @BsonProperty("writingLimitationsDetails")
        private WritingLimitationsDetails writingLimitationsDetails;

        private Boolean isUdid;

        private String udidNo;

        private String enrollmentNumber;

        private EnrollmentNumberDetails enrollmentNumberDetails;

        private String issuedOn;

        @BsonProperty("dis_cert_doc")
        private Boolean disCertDoc;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DisabilityType {

            private Integer disablityTypeId;

            private List<DisabilityTypeSubCategory> disablityTypeSubCategory;

            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class DisabilityTypeSubCategory {
                private Integer sid;
            }
        }

        /* =====================================================
           Writing Limitations Details
           ===================================================== */
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class WritingLimitationsDetails {
            private Integer writing_extremilyCertificateNo;
            @BsonProperty("wrt_extm_cert")
            private Integer wrt_extm_cert;
            private String certificateNo;
            private String issuedOn;
            private String issuingAuthorityDesignation;
            private Integer issuingAuthorityStateId;
            private String authorityAddress;
            private Boolean eligibilityCmp_timeId;
            private Boolean scribe_eligibility;
            private Boolean scribeService;
            private Boolean pwbdRecDoc;
        }
    }

    /* =====================================================
       PWBD REQUIREMENT EXAM
       ===================================================== */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PwbdRequirementExam {
        private String examName;
        private Integer examYear;
        private String rollNo;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnrollmentNumberDetails {
        
            private Integer pwd_application_id;
            private String application_status;
            private String application_number;
            private String udid_number;
            private String first_name;
            private String fullname;
            private String category;
            private String relation;
            private String father_name;
            private String mother_name;
            private String guardian_name;
            private String guardian_contact;
            private String employment_status;
            private String qualification;
            private String dob;
            private String gender;
            private String mobile_number;
            private String email_id;
            private String blood_group;
            private String current_address;
            private Integer lgdir_state_code;
            private Integer lgdir_district_code;
            private String lgdir_subdistrict_code;
            private Integer lgdir_city_code;
            private String address;
            private Integer pincode;
            private String disability_code;
            private String disability_by_birth;
            private String disability_since;
            private String disability_due_to;
            private String disability_details_serial_no;
            private String disability_details_issue_date;
            private String valid_upto;
            private String disability_condition_category;
            private String have_disability_cert;
            private String state_name;
            private String district_name;
            private String subdistrict_name;
            private String city_name;
            private String disability_type;
            private Integer disability_percentage;
            private String udid_valid_till;
            private String date_of_issue;
            private String disability_percent_option;
            private String diagnosis;
            private String affected_part;
            private String disability_cert_doc_couch_ext;
            private String disability_nature;
            private String disability_cert_doc;
            private Integer is_ekyc;
    }

}
