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
public class Community {

    @BsonProperty("CategoryId")
    private Integer categoryId;

    @BsonProperty("IsOBCCreamyLayer")
    private Boolean isObcCreamyLayer;

    @BsonProperty("CommDetails")
    private CommDetails commDetails;

    @BsonProperty("CasteDetails")
    private CasteDetails casteDetails;

    @BsonProperty("ser_post")
    private String serPost;

    @BsonProperty("updatedOn")
    private LocalDateTime updatedOn;


    @BsonProperty("i_on")
    private LocalDateTime createdOn;
}
