package in.nic.upscora.graphql.form4.mongo.entity.application;


import org.eclipse.microprofile.graphql.DefaultValue;

import io.smallrye.graphql.api.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Experience {
    
    private String experienceType;
    private String jobCapacity;
    private String kindOfExperience;
    private String fieldOfExperience;
    private String organizationTypes;
    private String payScale;
    private String lastSalary;
    private String orgName;
    private String address1;
    private String address2;
    private String address3;
    private String postingPlace;
    private String dateFrom;
    private String dateTo;
    
    @Nullable
    @DefaultValue("false")
    private boolean continuing;
    
    private String employmentTypes;
    private String supervisorLevel;
    private String natureOfDuties;
    private String experienceProof; 
}
