package in.nic.upscora.graphql.form4.mongo.entity.application;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDetails {

    private String requirementType;
    private String language;
    private String readProficiency;
    private String writeProficiency;
    private String speakProficiency;
    private String qualificationCourse;
    private String instituteName;
    private String place;
    private String dateOfCertification;
   
}
