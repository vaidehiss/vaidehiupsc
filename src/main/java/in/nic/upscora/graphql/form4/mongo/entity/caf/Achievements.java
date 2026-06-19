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
public class Achievements {

    private Boolean isAchievements;

    private String prizes;

    private String sports;

    private String position;

    @BsonProperty("ext_corr")
    private String extCorr;

    private LocalDateTime updatedOn;


    @BsonProperty("i_on")
    private LocalDateTime createdOn;
}
