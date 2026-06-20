package in.nic.upscora.graphql.form4.mongo.entity.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDetails {

    private String requirementType;
    private String certificateName;
    private String certificateNameOther;
    private String issuingAuthority;
    private String certificateNumber;
    private String dateOfIssue;
    private String validityType;
    private String validUntilDate;
    private String certificateDocument;
}
