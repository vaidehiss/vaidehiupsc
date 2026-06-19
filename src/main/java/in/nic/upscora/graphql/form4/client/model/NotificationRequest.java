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
public class NotificationRequest {

    @JsonProperty("event_code")
    private String eventCode;

    private Map<String, ChannelConfig> channels;
}
