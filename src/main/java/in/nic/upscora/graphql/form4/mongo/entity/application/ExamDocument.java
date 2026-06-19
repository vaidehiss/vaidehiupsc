package in.nic.upscora.graphql.form4.mongo.entity.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDocument {
    private String type;
    private String fileName;
    private String filePath;
}
