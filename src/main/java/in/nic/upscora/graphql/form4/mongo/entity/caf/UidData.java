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
public class UidData {

    @BsonProperty("is_uid")
    private Boolean isUid;

    @BsonProperty("i_on")
    private LocalDateTime createdOn;

    private LocalDateTime utc;

    @BsonProperty("u_d")
    private UidDetails uD;

    @BsonProperty("add_aadhar")
    private Boolean addAadhar;
}
