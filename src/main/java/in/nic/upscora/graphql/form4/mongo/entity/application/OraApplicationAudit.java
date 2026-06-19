package in.nic.upscora.graphql.form4.mongo.entity.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity(collection = "form4_audit_ora", clientName = "oradb")
public class OraApplicationAudit {

    @BsonId
    @BsonProperty("_id")
    private ObjectId id;

    private String applicant_urn;

    private String vacancyId;

    private List<AuditEntry> history;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuditEntry {
        private OraApplication oraApplication;
        private LocalDateTime deleted_at;
    }

    public void addEntry(AuditEntry entry) {
        if (history == null) {
            history = new ArrayList<>();
        }
        history.add(entry);
    }
}
