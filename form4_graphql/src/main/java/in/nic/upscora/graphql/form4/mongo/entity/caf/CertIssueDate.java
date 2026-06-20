package in.nic.upscora.graphql.form4.mongo.entity.caf;


import org.bson.codecs.pojo.annotations.BsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertIssueDate {

    @BsonProperty("Year")
    private Integer year;

    @BsonProperty("Month")
    private Integer month;

    @BsonProperty("Day")
    private Integer day;
}
