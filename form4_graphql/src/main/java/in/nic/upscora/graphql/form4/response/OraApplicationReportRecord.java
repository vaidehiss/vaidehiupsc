package in.nic.upscora.graphql.form4.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.graphql.Name;

/**
 * DTO for various breakup reports.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OraApplicationReportRecord {

    @Name("URN")
    private String applicant_urn;

    private Long applicationId;

    private String name;

    private String gender;

    @Name("mobileNo")
    private String mobileNo;

    @Name("emailID")
    private String email;

    private String submittedAt;

    private String community;

    @Name("paymentStatus")
    private String paymentStatus;

    @Name("paymentMode")
    private String paymentMode;

    private String amount;

    @Name("transactionID")
    private String transactionId;

    @Name("applicationStatus")
    private String applicationStatus;

    private String center;
}
