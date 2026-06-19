package in.nic.upscora.graphql.form4.request;

import java.util.List;

import org.eclipse.microprofile.graphql.NonNull;

import in.nic.upscora.graphql.form4.mongo.entity.application.Experience;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesirableExperience {
    
    @NonNull
    private AllRequestCommonInfo applicant_info;

    private String desirableExperienceApplicable;
    private List<Experience> desirable_experience;
    

}