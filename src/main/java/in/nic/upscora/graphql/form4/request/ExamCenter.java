package in.nic.upscora.graphql.form4.request;

import java.util.List;

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
public class ExamCenter {
    
    @NonNull
    private AllRequestCommonInfo applicant_info;

    private List<String> centerPreferences;

    @Nullable
    @DefaultValue("false")
    private boolean centerDeclarationAccepted;
    

}