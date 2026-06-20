package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Digidocs {

    @BsonProperty("bc")
    private Boolean bc;

    @BsonProperty("bc_hmac")
    private String bcHmac;

    @BsonProperty("bc_i_on_ist")
    private String bcIOnIst;

    @BsonProperty("pid")
    private Boolean pid;

    @BsonProperty("pid_hmac")
    private String pidHmac;

    @BsonProperty("cc")
    private Boolean cc;

    @BsonProperty("cc_hmac")
    private String ccHmac;

    @BsonProperty("cc_i_on_ist")
    private String ccIOnIst;

    @BsonProperty("obc")
    private Boolean obc;

    @BsonProperty("obc_hmac")
    private String obcHmac;

    @BsonProperty("pid_aadhaar")
    private Boolean pidAadhaar;

    @BsonProperty("pid_aadhaar_hmac")
    private String pidAadhaarHmac;

    @BsonProperty("shc")
    private Boolean shc;

    @BsonProperty("shc_hmac")
    private String shcHmac;
}
