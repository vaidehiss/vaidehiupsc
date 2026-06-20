package in.nic.upscora.graphql.form4.request;

import org.eclipse.microprofile.graphql.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileLock {

    @NonNull
    private AllRequestCommonInfo applicant_info;

}
