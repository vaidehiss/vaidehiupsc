package in.nic.upscora.graphql.form4.mongo.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity(collection = "ora_application_counter", clientName = "oradb")
public class OraApplicationCounter {

    @BsonId
    @BsonProperty("_id")
    private String id;

    private Long counter;
}
