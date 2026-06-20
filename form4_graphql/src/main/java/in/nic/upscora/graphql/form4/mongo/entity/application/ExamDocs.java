package in.nic.upscora.graphql.form4.mongo.entity.application;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDocs {
    private List<ExamDocument> cafDocs;
    private List<ExamDocument> essentialQualification;
    private List<ExamDocument> desirableQualification;
    private List<ExamDocument> additionalQualification;
    private List<ExamDocument> essentialExperience;
    private List<ExamDocument> desirableExperience;
    private List<ExamDocument> additionalExperience;
    private List<ExamDocument> diplomaEssential;
    private List<ExamDocument> diplomaDesirable;
    private List<ExamDocument> internshipEssential;
    private List<ExamDocument> internshipDesirable;
    private List<ExamDocument> languageEssential;
    private List<ExamDocument> languageDesirable;
    private List<ExamDocument> certificateEssential;
    private List<ExamDocument> certificateDesirable;
    private List<ExamDocument> professionalRegistrationEssential;
    private List<ExamDocument> professionalRegistrationDesirable;
    private List<ExamDocument> gateEssential;
    private List<ExamDocument> gateDesirable;
    private List<ExamDocument> netEssential;
    private List<ExamDocument> netDesirable;
    private List<ExamDocument> conferenceEssential;
    private List<ExamDocument> conferenceDesirable;
    private List<ExamDocument> publicationEssential;
    private List<ExamDocument> publicationDesirable;
    private List<ExamDocument> mergedDocument;
}
