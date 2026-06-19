package in.nic.upscora.graphql.form4.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.graphql.Name;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonCandidateReportRecord {

    @Name("URN")
    private String applicant_urn;

    private String name;

    @Name("mobileNo")
    private String mobileNo;

    @Name("emailID")
    private String email;

    @Name("advertisementNo")
    private String advertisementNo;

    @Name("vacancyNo")
    private String vacancyNo;

    @Name("applicationIDs")
    private String applicationIds;
}
