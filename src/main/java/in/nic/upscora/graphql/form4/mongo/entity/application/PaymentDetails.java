package in.nic.upscora.graphql.form4.mongo.entity.application;


import org.eclipse.microprofile.graphql.Name;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {

//    private String payment_mode;
//    private String payment_status;
//    private String transactionId;
//    private Integer amount;
//    private String paid_at;

    @Name("paymentStatus")
    private String status;
    private String unique_ref_no;
    @Name("transactionId")
    private String transaction_id;
    @Name("paymentMode")
    private String payment_mode;
    private String exam_code;
    private String amount;
    @Name("paymentDateTime")
    private String transaction_date;
    private String description;
    private String ref_id;

   
}
