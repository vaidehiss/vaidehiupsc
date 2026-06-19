package in.nic.upscora.graphql.form4.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelConfig {

    private String recipient;

    @JsonProperty("template_name")
    private String templateName;

    private Map<String, String> variables;

    private Metadata metadata;
}
