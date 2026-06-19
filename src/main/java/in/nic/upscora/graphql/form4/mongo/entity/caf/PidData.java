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
public class PidData {

    @BsonProperty("is_pid")
    private Boolean isPid;

    private Integer pid;

    private String no;

    @BsonProperty("pid_doc")
    private Boolean pidDoc;

    @BsonProperty("issued_on")
    private String issuedOn;

    @BsonProperty("exp_dt")
    private String expDt;

    @BsonProperty("aadhaarAddress")
    private String aadhaarAddress;

    @BsonProperty("aadhaarDob")
    private String aadhaarDob;

    @BsonProperty("aadhaarName")
    private String aadhaarName;

    @BsonProperty("aadhaarUid")
    private String aadhaarUid;

    @BsonProperty("pid_verification")
    private Boolean pidVerification;

    private LocalDateTime utc;

    @BsonProperty("i_on")
    private LocalDateTime createdOn;
}
