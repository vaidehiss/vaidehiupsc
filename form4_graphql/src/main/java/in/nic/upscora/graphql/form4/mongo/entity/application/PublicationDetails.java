package in.nic.upscora.graphql.form4.mongo.entity.application;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicationDetails {

    private String requirementType;
    private String natureOfPublication;
    private String kindOfJournal;
    private String kindOfAuthorship;
    private String articleTitle;
    private String journalName;
    private String year;
    private String volume;
    private String issue;
    private String pages;
   
}
