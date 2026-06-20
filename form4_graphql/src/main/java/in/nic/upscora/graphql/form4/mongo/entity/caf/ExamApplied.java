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
public class ExamApplied {

    @BsonProperty("application_id")
    private Long applicationId;

    @BsonProperty("exam_name")
    private String examName;

    @BsonProperty("exam_id")
    private String examId;

    // @BsonProperty("exam_year")
    // private Integer examYear;

    @BsonProperty("exam_code")
    private String examCode;

    @BsonProperty("submitted_ist")
    private LocalDateTime submittedIst;

    // private Payment payment;
    //
    // @Data
    // @Builder
    // @NoArgsConstructor
    // @AllArgsConstructor
    // public static class Payment {
    //
    //     @BsonProperty("applicant_id")
    //     private Long applicantId;
    //
    //     @BsonProperty("exam_code")
    //     private String examCode;
    //
    //     @BsonProperty("exam_year")
    //     private Integer examYear;
    //
    //     @BsonProperty("exam_id")
    //     private String examId;
    //
    //     @BsonProperty("application_no")
    //     private String applicationNo;
    //
    //     @BsonProperty("application_id")
    //     private Long applicationId;
    //
    //     @BsonProperty("transaction_id")
    //     private String transactionId;
    //
    //     @BsonProperty("payment_datetime")
    //     private String paymentDatetime;
    //
    //     @BsonProperty("payment_method")
    //     private String paymentMethod;
    //
    //     private Double amount;
    //
    //     private String location;
    //
    //     @BsonProperty("i_on")
    //     private String createdOn;
    //
    //     @BsonProperty("i_on_ist")
    //     private String createdOnIst;
    //
    //     @BsonProperty("_id")
    //     private String id;
    //     
    //
    // }
}
