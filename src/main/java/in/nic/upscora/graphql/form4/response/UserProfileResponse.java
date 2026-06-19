package in.nic.upscora.graphql.form4.response;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    
    public String email;

    public String mobileNum;

    public Boolean isProfileComplete;

    public LocalDateTime cafSubmitIon;
}
