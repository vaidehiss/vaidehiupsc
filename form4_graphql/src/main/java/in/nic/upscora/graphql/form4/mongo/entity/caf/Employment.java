package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employment {

    @BsonProperty("is_self")
    private Boolean isSelf;

    @BsonProperty("self")
    private List<SelfEmploymentRecord> self;

    @BsonProperty("nSelfStatus")
    private Boolean nSelfStatus;

    @BsonProperty("nSelf")
    private List<NonSelfEmploymentRecord> nSelf;

    @BsonProperty("i_on")
    private LocalDateTime createdOn;

    @BsonProperty("updatedOnUTC")
    private LocalDateTime updatedOnUTC;

    @BsonProperty("updatedOn")
    private LocalDateTime updatedOn;

    /* =====================================================
       SELF EMPLOYMENT
       ===================================================== */

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelfEmploymentRecord {

        @BsonProperty("monthlyIncome")
        private Double monthlyIncome;

        @BsonProperty("fromDate")
        private String fromDate;

        @BsonProperty("toDate")
        private String toDate;

        @BsonProperty("employmentStatus")
        private Integer employmentStatus;

        @BsonProperty("companyPlace")
        private String companyPlace;

        @BsonProperty("employerName")
        private String employerName;

        @BsonProperty("employment_nature")
        private String employmentNature;

        @BsonProperty("remarks")
        private String remarks;
    }


    /* =====================================================
       NON-SELF EMPLOYMENT
       ===================================================== */

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NonSelfEmploymentRecord {

        @BsonProperty("employerType")
        private Integer employerType;

        @BsonProperty("designation")
        private String designation;

        @BsonProperty("monthlyIncome")
        private Double monthlyIncome;

        @BsonProperty("employmentTypeId")
        private Integer employmentTypeId;

        @BsonProperty("employmentTypeName")
        private String employmentTypeName;

        @BsonProperty("employerName")
        private String employerName;

        @BsonProperty("fromDate")
        private String fromDate;

        @BsonProperty("employmentStatus")
        private Integer employmentStatus;

        @BsonProperty("toDate")
        private String toDate;

        @BsonProperty("experienceType")
        private Integer experienceType;

        @BsonProperty("companyPlace")
        private String companyPlace;

        @BsonProperty("natureOfDuties")
        private String natureOfDuties;

        @BsonProperty("workedAtId")
        private Integer workedAtId;

        @BsonProperty("isWorkedAsSupAdmin")
        private String isWorkedAsSupAdmin;

        @BsonProperty("workedAsSupAdmin")
        private Integer workedAsSupAdmin;

        @BsonProperty("otherWorkName")
        private String otherWorkName;

        @BsonProperty("otherEmpTypeName")
        private String otherEmpTypeName;

        @BsonProperty("intimationDate")
        private String intimationDate;
    }
}
