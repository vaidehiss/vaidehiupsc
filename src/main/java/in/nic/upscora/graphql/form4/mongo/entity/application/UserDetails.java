package in.nic.upscora.graphql.form4.mongo.entity.application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {

    @BsonProperty("email")
    private String email;

    @BsonProperty("mobileNo")
    private Long mobileNo;
}
