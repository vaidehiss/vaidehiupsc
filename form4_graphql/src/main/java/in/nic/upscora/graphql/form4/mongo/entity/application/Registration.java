package in.nic.upscora.graphql.form4.mongo.entity.application;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Registration {

    private String requirementType;
    private String kindOfRegistration;
    private String registeredWith;
    private String registrationNo;
    private String dateOfRegistration;
    private String registrationType;
    private String validityType;
    private String validUptoDate;
    private String regCertificate;
   
}
