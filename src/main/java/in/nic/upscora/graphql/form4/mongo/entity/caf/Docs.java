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
public class Docs {

    // ===== Upload flags =====

    @BsonProperty("bc")
    private Boolean bc;

    @BsonProperty("aadhar_im")
    private Boolean aadharIm;

    @BsonProperty("cc")
    private Boolean cc;

    @BsonProperty("photo")
    private Boolean photo;

    @BsonProperty("s")
    private Boolean s;

    @BsonProperty("pid")
    private Boolean pid;

    @BsonProperty("dc")
    private Boolean dc;

    @BsonProperty("pw_rec")
    private Boolean pwRec;

    @BsonProperty("live_photo")
    private Boolean livePhoto;

    @BsonProperty("triple_signature")
    private Boolean tripleSignature;

    @BsonProperty("pid_digilocker")
    private Boolean pidDigilocker;


    // ===== S3 URLs =====

    @BsonProperty("s3_aadhar_im_url")
    private String s3AadharImUrl;

    @BsonProperty("s3_bc_url")
    private String s3BcUrl;

    @BsonProperty("s3_cc_url")
    private String s3CcUrl;

    @BsonProperty("s3_dc_url")
    private String s3DcUrl;

    @BsonProperty("s3_pid_url")
    private String s3PidUrl;

    @BsonProperty("s3_photo_url")
    private String s3PhotoUrl;

    @BsonProperty("s3_s_url")
    private String s3SignUrl;

    @BsonProperty("s3_live_photo_url")
    private String s3LivePhotoUrl;

    @BsonProperty("s3_pw_rec_url")
    private String s3PwRecUrl;


    // ===== Extra metadata =====

    @BsonProperty("photo_insert_ist")
    private String photoInsertIst;

    @BsonProperty("s_insert_ist")
    private String sInsertIst;

    @BsonProperty("cc_insert_ist")
    private String ccInsertIst;

    @BsonProperty("bc_insert_ist")
    private String bcInsertIst;

    @BsonProperty("aadhar_im_insert_ist")
    private String aadharImInsertIst;

    @BsonProperty("pid_insert_ist")
    private String pidInsertIst;

    @BsonProperty("dc_insert_ist")
    private String dcInsertIst;

    @BsonProperty("pw_rec_insert_ist")
    private String pwRecInsertIst;

    @BsonProperty("live_photo_insert_ist")
    private String livePhotoInsertIst;

    @BsonProperty("live_photo_webcam_ist")
    private String livePhotoWebcamIst;

    @BsonProperty("ai_signature_s3_insert_ist")
    private String aiSignatureS3InsertIst;

    @BsonProperty("ai_signature_s3_url")
    private String aiSignatureS3Url;

    @BsonProperty("name_change")
    private Boolean nameChange;

    @BsonProperty("name_change_insert_ist")
    private String nameChangeInsertIst;

    @BsonProperty("s3_name_change_url")
    private String s3NameChangeUrl;
}
