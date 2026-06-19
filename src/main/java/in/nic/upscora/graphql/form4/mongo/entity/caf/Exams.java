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
public class Exams {

    private Boolean debaardStatus;

    private String debarredDetails;

    @BsonProperty("examhistorystatus")
    private Boolean examHistoryStatus;

    private List<ExamHistory> examHistory;

    @BsonProperty("i_on")
    private LocalDateTime createdOn;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExamHistory {

        private String examName;

        @BsonProperty("exam_id")
        private Integer examId;

        private Integer examYear;

        private String rollNo;

        private Boolean interviewStatus;

        private Boolean appoointmentStatus;

        private Boolean joinedStatus;

        private JointDetails jointDetails;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class JointDetails {

            private Boolean isContinuationService;

            private Boolean isResigned;

            private String resingnedDetails;
        }
    }
}
