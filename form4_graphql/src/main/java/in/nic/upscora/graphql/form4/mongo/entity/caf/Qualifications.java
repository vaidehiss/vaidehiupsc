package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Qualifications {

    private List<Qualification> qualifications;

    @BsonProperty("i_on")
    private LocalDateTime createdOn;

    @BsonProperty("updatedOn")
    private LocalDateTime updatedOn;
}
