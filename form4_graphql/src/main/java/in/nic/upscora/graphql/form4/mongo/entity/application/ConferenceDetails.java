package in.nic.upscora.graphql.form4.mongo.entity.application;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConferenceDetails {

    private String requirementType;
    private String natureOfConference;
    private String conferenceTitle;
    private String organizedBy;
    private String year;
   
}
