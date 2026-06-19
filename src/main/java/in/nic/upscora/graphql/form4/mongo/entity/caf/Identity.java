package in.nic.upscora.graphql.form4.mongo.entity.caf;

import java.time.LocalDateTime;

import org.bson.codecs.pojo.annotations.BsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Identity {

    // ===== Board / Country =====
    @BsonProperty("is_boardExam")
    private String isBoardExam;

    private Integer stateId;

    @BsonProperty("ConformStateId")
    private Integer conformStateId;

    @BsonProperty("districtId")
    private Integer districtId;

    @BsonProperty("confirmdistrictId")
    private Integer confirmdistrictId;

    @BsonProperty("countryId")
    private Integer countryId;

    private String countryName;
    private String otherCountryName;

    @BsonProperty("forignEducateBoard_nm")
    private String forignEducateBoardNm;


    // ===== Board Details =====
    private Integer educationalBoardId;

    private String otherEducationalBoardName;

    @BsonProperty("coformEducationalBoardId")
    private Integer coformEducationalBoardId;

    private Integer educationalBoardYear;

    @BsonProperty("ceb_yr")
    private Integer cebYr;


    // ===== Certificate =====
    private String educationalBoardCertificateNumber;

    private String conformEducationalBoardCertificateNumber;

    private String educationalBoardCertificateIssueDate;

    private String conformEducationalBoardCertificateIssueDate;


    // ===== Student Info =====
    private String dob;

    private String rollNumber;

    @BsonProperty("cr_no")
    private String crNo;

    private Integer gradeSystemId;

    private Integer gpaId;

    private Double marks;

    @BsonProperty("school_udise_code")
    private String schoolUdiseCode;

    @BsonProperty("school_name")
    private String schoolName;


    // ===== Flags =====
    private Boolean isBoardCertificateUploaded;

    private Boolean isDeclared;


    // ===== Nested cert_issue_dt =====
    @BsonProperty("cert_issue_dt")
    private CertIssueDate certIssueDt;

    private LocalDateTime utc;

    private LocalDateTime updatedOn;

    private Boolean missing;


    // ===== Audit =====
    @BsonProperty("i_on")
    private LocalDateTime createdOn;
}
