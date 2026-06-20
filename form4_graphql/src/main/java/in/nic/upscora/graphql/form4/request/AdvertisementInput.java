package in.nic.upscora.graphql.form4.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import org.eclipse.microprofile.graphql.Name;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementInput {
    @Name("advertisementNo")
    private String advertisementNo;
    
    @Name("vacancyNumbers")
    private List<String> vacancyNumbers;
}
