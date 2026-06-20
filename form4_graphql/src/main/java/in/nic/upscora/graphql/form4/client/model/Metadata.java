package in.nic.upscora.graphql.form4.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

    @JsonProperty("ttl_seconds")
    private Integer ttlSeconds;
}
