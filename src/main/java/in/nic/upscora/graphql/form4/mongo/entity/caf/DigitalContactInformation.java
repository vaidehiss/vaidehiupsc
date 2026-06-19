package in.nic.upscora.graphql.form4.mongo.entity.caf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalContactInformation {

    private String alternateEmailId;

    private String alternateMobileNo;

    private String alternateWhatsupNo;
}
