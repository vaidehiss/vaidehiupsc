package in.nic.upscora.graphql.form4.mongo.entity.application;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetQualification {
    
   private String requirementType;
    private String isNetQualified;
    private String conductedBy;
    private String subjectSelect;
    private String subjectInput;
    private String dateOfExamination;
    private String dateOfResult;
    private String dateOfIssue;
    private String marksObtained;
    private String netCertificate;
}
