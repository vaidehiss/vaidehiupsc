package in.nic.upscora.graphql.form4.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentUploadDetails {

    private String bucket;
    private String key;
    private String archived_to;
    private String type;
    private Long applicant_id;
    private Long advertisement_id;
    private String lang;
    private String type_of_document;
    private String face_quality;
}
