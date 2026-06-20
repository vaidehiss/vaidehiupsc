package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIdentity {

    private Boolean isNameSameAsBoard;

    private String firstName;

    @BsonProperty("cf_nm")
    private String cfNm;

    private String middleName;

    @BsonProperty("cm_nm")
    private String cmNm;

    private String lastName;

    @BsonProperty("cl_nm")
    private String clNm;

    private String boardFirstName;

    @BsonProperty("cb_f_nm")
    private String cbFnm;

    private String boardMiddleName;

    @BsonProperty("cb_m_nm")
    private String cbMnm;

    private String boardLastName;

    @BsonProperty("cb_l_nm")
    private String cbLnm;

    private String fatherName;

    @BsonProperty("cfath_nm")
    private String cfathNm;

    private String guardianName;

    @BsonProperty("cguar_nm")
    private String cguarNm;

    private String motherName;

    @BsonProperty("cmoth_nm")
    private String cmothNm;

    private String dateOfBirth;

    @BsonProperty("cdob")
    private String cDob;

    private Integer genderId;

    @BsonProperty("cg_id")
    private Integer cgId;

    @BsonProperty("gazetteDoc_consent")
    private Boolean gazetteDocConsent;

    private Boolean gazetteDoc;

    private Integer gazetteStateId;
    private String gazetteStateOtherName;
    private Integer gazetteDistrictId;
    private String gazetteDistrictName;

    private String gazetteIssuedDate;
    private String gazetteNotificationNumber;

    private LocalDateTime utc;

    private LocalDateTime updatedOn;

    @BsonProperty("i_on")
    private LocalDateTime createdOn;
}
