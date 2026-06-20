package in.nic.upscora.graphql.form4.request;

import org.eclipse.microprofile.graphql.NonNull;

import in.nic.upscora.graphql.form4.mongo.entity.application.AssistiveDeviceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgeRelaxation {
    
    @NonNull
    private AllRequestCommonInfo applicant_info;

    private String claimAgeRelaxation;
    private String relaxationCategoryCode;
    private String supportDocument;
    private String biggerFontSize;
    private String compensatoryTime;
    private String wantScribe;
    private String assistiveDevice;
    private AssistiveDeviceType assistiveDevices;


}