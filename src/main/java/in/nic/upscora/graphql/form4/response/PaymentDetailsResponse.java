package in.nic.upscora.graphql.form4.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.graphql.Name;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailsResponse {

    private String amount;
    
    @Name("transaction_date")
    private String transaction_date;
    
    @Name("transaction_id")
    private String transaction_id;
    
    @Name("payment_mode")
    private String payment_mode;
}
