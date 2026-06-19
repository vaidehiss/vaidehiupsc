package in.nic.upscora.graphql.form4.mongo.entity.caf;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NegdLiveVsUploadedVerification {

    @BsonProperty("face_matches")
    private List<FaceMatch> faceMatches;

    @BsonProperty("source_image_face")
    private SourceImageFace sourceImageFace;

    @BsonProperty("unmatched_faces")
    private List<FaceDetails> unmatchedFaces;

    @BsonProperty("verification_timestamp")
    private String verificationTimestamp;

    @BsonProperty("match_decision")
    private String matchDecision;

    @BsonProperty("similarity_threshold_used")
    private Integer similarityThresholdUsed;

    @BsonProperty("comparison_type")
    private String comparisonType;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FaceMatch {

        @BsonProperty("Face")
        private FaceDetails face;

        @BsonProperty("Similarity")
        private Double similarity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceImageFace {

        @BsonProperty("BoundingBox")
        private BoundingBox boundingBox;

        @BsonProperty("Confidence")
        private Double confidence;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FaceDetails {

        @BsonProperty("BoundingBox")
        private BoundingBox boundingBox;

        @BsonProperty("Confidence")
        private Double confidence;

        @BsonProperty("Landmarks")
        private List<Landmark> landmarks;

        @BsonProperty("Pose")
        private Pose pose;

        @BsonProperty("Quality")
        private Quality quality;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoundingBox {
        @BsonProperty("Height")
        private Double height;

        @BsonProperty("Left")
        private Double left;

        @BsonProperty("Top")
        private Double top;

        @BsonProperty("Width")
        private Double width;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Landmark {
        @BsonProperty("Type")
        private String type;

        @BsonProperty("X")
        private Double x;

        @BsonProperty("Y")
        private Double y;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pose {
        @BsonProperty("Pitch")
        private Double pitch;

        @BsonProperty("Roll")
        private Double roll;

        @BsonProperty("Yaw")
        private Double yaw;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Quality {
        @BsonProperty("Brightness")
        private Double brightness;

        @BsonProperty("Sharpness")
        private Double sharpness;
    }
}
