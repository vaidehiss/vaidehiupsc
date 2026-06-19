package in.nic.upscora.graphql.form4.mongo.entity.application.info;

import java.time.LocalDateTime;

import org.eclipse.microprofile.graphql.DefaultValue;

import in.nic.upscora.graphql.form4.request.AllRequestCommonInfo;
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
public class OraApplicationInfo {

    @NotBlank
    @NotEmpty
    @NotNull
    private String recruitmentYear;

    @NotBlank
    @NotEmpty
    @NotNull
    private String recruitmentCode;

    @NotBlank
    @NotEmpty
    @NotNull
    private String post_name;

    @NotBlank
    @NotEmpty
    @NotNull
    private String stepKey;

    @NotBlank
    @NotEmpty
    @NotNull
    private String status;

    @Nullable
    @DefaultValue("false")
    private boolean cafLocked;

    @Nullable
    @DefaultValue("false")
    private boolean profileLocked;

    @Nullable
    @DefaultValue("false")
    private boolean centerDeclarationAccepted;

    private LocalDateTime submittedAt;

    @Nullable
    @DefaultValue("false")
    private boolean submitDeclarationAccepted;

    public OraApplicationInfo(AllRequestCommonInfo commonInfo) {

        this.setPost_name(commonInfo.getPost_name());
        this.setRecruitmentCode(commonInfo.getRecruitmentCode());
        this.setRecruitmentYear(commonInfo.getRecruitmentYear());
        this.setStatus("DRAFT");
        this.setStepKey(commonInfo.getStepKey());

    }

}
