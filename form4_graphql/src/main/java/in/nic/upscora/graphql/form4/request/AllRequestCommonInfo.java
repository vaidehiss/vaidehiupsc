package in.nic.upscora.graphql.form4.request;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.NonNull;

import io.smallrye.graphql.api.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllRequestCommonInfo {

    @NonNull
    @NotBlank
    @NotEmpty
    @NotNull
    private String applicant_urn;
    
    @NonNull
    @NotBlank
    @NotEmpty
    @NotNull
    private String vacancyId;
    
    @NonNull
    @NotBlank
    @NotEmpty
    @NotNull
    private String recruitmentYear;
    
    @NonNull
    @NotBlank
    @NotEmpty
    @NotNull
    private String recruitmentCode;
    
    @NonNull
    @NotBlank
    @NotEmpty
    @NotNull
    private String post_name;

    @NonNull
    @NotBlank
    @NotEmpty
    @NotNull
    private String stepKey;

    @Nullable
    @DefaultValue("false")
    private boolean profileLocked;

}
