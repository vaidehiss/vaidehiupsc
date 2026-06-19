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
public class CasteDetails {

    @BsonProperty("CasteName")
    private String casteName;

    @BsonProperty("CenterListNumber")
    private String centerListNumber;
}
