package in.nic.upscora.graphql.form4.request;


import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.NonNull;


import io.smallrye.graphql.api.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitApplication {
    
    @NonNull
    private AllRequestCommonInfo applicant_info;


    private String status;

    private String submittedAt;

    @Nullable
    @DefaultValue("false")
    private boolean submitDeclarationAccepted;
    

}