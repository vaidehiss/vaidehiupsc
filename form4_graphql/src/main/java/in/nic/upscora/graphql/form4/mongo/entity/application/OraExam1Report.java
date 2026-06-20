package in.nic.upscora.graphql.form4.mongo.entity.application;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity(collection = "ora_exam1_report", clientName = "oradb")
public class OraExam1Report {

    @BsonId
    @BsonProperty("_id")
    private String id;

    private String vacancyId;

    private String vacancyNumber;

    private String vacancyTitle;

    @Builder.Default
    private Map<String, Long> applicationStatusBreakup = new LinkedHashMap<>();

    @Builder.Default
    private Map<String, BucketCount> centerWise = new LinkedHashMap<>();

    @Builder.Default
    private Map<String, BucketCount> communityGenderMatrix = new LinkedHashMap<>();

    @Builder.Default
    private Map<String, BucketCount> communityWise = new LinkedHashMap<>();

    @Builder.Default
    private Map<String, BucketCount> genderWise = new LinkedHashMap<>();

    @Builder.Default
    private Map<String, Long> paymentBreakup = new LinkedHashMap<>();

    @Builder.Default
    private PaymentDetailsBreakup paymentDetailsBreakup = new PaymentDetailsBreakup();

    @Builder.Default
    private List<CenterPreferenceBreakup> centerPreferenceBreakup = new ArrayList<>();

    private LocalDateTime lastUpdated;

    @BsonProperty("last_updated_ist")
    private String lastUpdatedIst;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BucketCount {
        @Builder.Default
        private Long pendingCount = 0L;

        @Builder.Default
        private Long submittedCount = 0L;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDetailsBreakup {
        @Builder.Default private Long upi = 0L;
        @Builder.Default private Long creditCard = 0L;
        @Builder.Default private Long debitCard = 0L;
        @Builder.Default private Long netbankingSbi = 0L;
        @Builder.Default private Long netbankingOthers = 0L;
        @Builder.Default private Long exempted = 0L;
        @Builder.Default private Long totalPaid = 0L;
        @Builder.Default private Long total = 0L;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CenterPreferenceBreakup {
        @Builder.Default private String centerCode = "";
        @Builder.Default private String centerName = "";
        @Builder.Default private Long firstPreferenceCount = 0L;
        @Builder.Default private Long secondPreferenceCount = 0L;
    }
}
