package in.nic.upscora.graphql.form4.mongo.entity.application;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternshipDetails {

    private String requirementType;
    private String completedInternship;
    private String instituteName;
    private String instituteNameOther;
    private String dateFrom;
    private String dateTo;
    private String internshipDocument;
   
}
