package in.nic.upscora.graphql.form4.mongo.entity.userprofile;


import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import java.time.LocalDateTime;
import java.util.List;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity(collection = "users", clientName = "userdb")
public class UserProfile {

    @BsonId
    @BsonProperty("_id")
    private Long id;

    private String email;

    private Long mobileNo;

    private String password;

    private String ip;

    private String creationDate;

    private String updationDate;

    private String lastActivityDate;

    private String lastLoginDate;

    @BsonProperty("CandidateName")
    private String candidateName;

    @BsonProperty("IsProfileComplete")
    private Boolean isProfileComplete;

    @BsonProperty("caf_submit_ion")
    private LocalDateTime cafSubmitIon;

    private String lastLoginIp;

    @BsonProperty("password_history")
    private List<String> passwordHistory;

    private List<LoginTrail> loginTrails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginTrail {

        private String ip;

        @BsonProperty("login_date_time_ist")
        private String loginDateTimeIst;
    }
    
}
