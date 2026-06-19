package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommDetails {

    @BsonProperty("ceritficateNumber")
    private String ceritficateNumber;

    @BsonProperty("state_of_issue_authority")
    private Integer state_of_issue_authority;

    @BsonProperty("office_issue_authority")
    private String office_issue_authority;

    @BsonProperty("office_desigation_authority")
    private Integer office_desigation_authority;

    @BsonProperty("district_of_issue_authority")
    private Integer iss_auth_dist_id;

    @BsonProperty("address_issuse_authority")
    private String address_issuse_authority;

    @BsonProperty("issue_date")
    private String issue_date;

    @BsonProperty("religon_id")
    private Integer religon_id;

    @BsonProperty("IsFatherPan")
    private Boolean isFatherPan;

    @BsonProperty("FatherPan")
    private String fatherPan;

    @BsonProperty("IsMotherPan")
    private Boolean isMotherPan;

    @BsonProperty("MotherPan")
    private String motherPan;

    @BsonProperty("IsFatherAadhar")
    private Boolean isFatherAadhar;

    @BsonProperty("FatherAadhar")
    private String fatherAadhar;

    @BsonProperty("IsMotherAadhar")
    private Boolean isMotherAadhar;

    @BsonProperty("MotherAadhar")
    private String motherAadhar;

    @BsonProperty("FamilyPanCard")
    private List<FamilyPanCardDetails> familyPanCard;

    @BsonProperty("CasteCeritificateDoc")
    private Boolean casteCeritificateDoc;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FamilyPanCardDetails {

        @BsonProperty("MemberID")
        private Integer memberID;

        @BsonProperty("IsPan")
        private Boolean isPan;

        @BsonProperty("PanNumebr")
        private String panNumebr;

        @BsonProperty("IsAadhar")
        private Boolean isAadhar;

        @BsonProperty("AadharNumber")
        private String aadharNumber;
    }
}
