package in.nic.upscora.graphql.form4.request;

import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.NonNull;

import in.nic.upscora.graphql.form4.mongo.entity.application.PaymentDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInput {
    
    @NonNull
    private AllRequestCommonInfo applicant_info;


    @Name("paymentDetails")
    private PaymentDetails payment_details;

    // private String paymentMode;
    // private String paymentStatus;
    // private String transactionId;
    // private Integer amount;
    // private String paymentDateTime; 
    

}
