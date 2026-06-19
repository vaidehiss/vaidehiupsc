package in.nic.upscora.graphql.form4.mongo.entity.application;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiplomaDetails {

    private String requirementType;
    private String diplomaName;
    private String issuingAuthority;
    private String specialization;
    private String passingYear;
    private String resultType;
    private String score;
    private String durationYears;
    private String diplomaDocument;
    
   
}
