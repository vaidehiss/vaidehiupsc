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
public class PersonalProfile {

    private Boolean isMiniority;

    private Integer isMiniorityId;

    private Integer nationalityId;

    private Integer mothertongue;

    private String othermothertongue;

    private Integer domocileStateId;

    private Integer isMarriedId;

    private Integer spouseNatioinalityId;

    private String spouseNatioinalityName;

    private Integer birthPlaceId;

    private BirthPlaceDetails birthPlaceDetails;

    private DigitalContactInformation digitalContactInformation;
    private Boolean documentverificantion;

    private LocalDateTime updatedOn;

    @BsonProperty("updatedOnUTC")
    private LocalDateTime updatedOnUTC;

    @BsonProperty("i_on")
    private LocalDateTime createdOn;
}
