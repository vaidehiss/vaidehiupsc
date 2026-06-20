package in.nic.upscora.graphql.form4.mongo.entity.vacancy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MongoEntity(collection = "vacancy_2026", clientName = "vacancy2026")
public class Vacancy {

    @BsonId
    public ObjectId id;

    public String vacancyId;
    public String vacancyNumber;
    public String ministry;
    public String department;
    public String organization;
    public String postName;
    public Integer noOfPosts;
    // "openingDate": ""
    public LocalDateTime openingDate;
    // "closingDate": "28/02/2026 22:47"
    public LocalDateTime closingDate;
    // "ageCalculationDate": "01/03/2026"
    public LocalDate ageCalculationDate;
    // "experienceCalculationDate": "01/02/2026"
    public LocalDate experienceCalculationDate;
    public String fileNo;
    public String advertisementId;
    public String advertisementNo;
    public String createdBy;
    public String section;
    public boolean detailsFilled;
    public boolean deployedInStaging;
    public boolean deployedInProd;
    // "createdAt": "2026-02-01T17:18:19.665Z"
    public LocalDateTime createdAt;
    // "lastEditedAt": "2026-02-01T17:18:19.665Z"
    public LocalDateTime lastEditedAt;
    // "printingDate": "01/03/2026 22:48"
    public LocalDateTime printingDate;
    // public FormData formData;

    /**
     * @return the vacancyId
     */
    public String getVacancyId() {
        return vacancyId;
    }

    /**
     * @param vacancyId the vacancyId to set
     */
    public void setVacancyId(String vacancyId) {
        this.vacancyId = vacancyId;
    }

    /**
     * @return the vacancyNumber
     */
    public String getVacancyNumber() {
        return vacancyNumber;
    }

    /**
     * @param vacancyNumber the vacancyNumber to set
     */
    public void setVacancyNumber(String vacancyNumber) {
        this.vacancyNumber = vacancyNumber;
    }

    /**
     * @return the ministry
     */
    public String getMinistry() {
        return ministry;
    }

    /**
     * @param ministry the ministry to set
     */
    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the organization
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * @param organization the organization to set
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * @return the postName
     */
    public String getPostName() {
        return postName;
    }

    /**
     * @param postName the postName to set
     */
    public void setPostName(String postName) {
        this.postName = postName;
    }

    /**
     * @return the noOfPosts
     */
    public Integer getNoOfPosts() {
        return noOfPosts;
    }

    /**
     * @param noOfPosts the noOfPosts to set
     */
    public void setNoOfPosts(Integer noOfPosts) {
        this.noOfPosts = noOfPosts;
    }

    /**
     * @return the openingDate
     */
    public LocalDateTime getOpeningDate() {
        return openingDate;
    }

    /**
     * @param openingDate the openingDate to set
     */
    public void setOpeningDate(LocalDateTime openingDate) {
        this.openingDate = openingDate;
    }

    /**
     * @return the closingDate
     */
    public LocalDateTime getClosingDate() {
        return closingDate;
    }

    /**
     * @param closingDate the closingDate to set
     */
    public void setClosingDate(LocalDateTime closingDate) {
        this.closingDate = closingDate;
    }

    /**
     * @return the ageCalculationDate
     */
    public LocalDate getAgeCalculationDate() {
        return ageCalculationDate;
    }

    /**
     * @param ageCalculationDate the ageCalculationDate to set
     */
    public void setAgeCalculationDate(LocalDate ageCalculationDate) {
        this.ageCalculationDate = ageCalculationDate;
    }

    /**
     * @return the experienceCalculationDate
     */
    public LocalDate getExperienceCalculationDate() {
        return experienceCalculationDate;
    }

    /**
     * @param experienceCalculationDate the experienceCalculationDate to set
     */
    public void setExperienceCalculationDate(LocalDate experienceCalculationDate) {
        this.experienceCalculationDate = experienceCalculationDate;
    }

    /**
     * @return the fileNo
     */
    public String getFileNo() {
        return fileNo;
    }

    /**
     * @param fileNo the fileNo to set
     */
    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    /**
     * @return the advertisementId
     */
    public String getAdvertisementId() {
        return advertisementId;
    }

    /**
     * @param advertisementId the advertisementId to set
     */
    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    /**
     * @return the advertisementNo
     */
    public String getAdvertisementNo() {
        return advertisementNo;
    }

    /**
     * @param advertisementNo the advertisementNo to set
     */
    public void setAdvertisementNo(String advertisementNo) {
        this.advertisementNo = advertisementNo;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the section
     */
    public String getSection() {
        return section;
    }

    /**
     * @param section the section to set
     */
    public void setSection(String section) {
        this.section = section;
    }

    /**
     * @return the detailsFilled
     */
    public boolean isDetailsFilled() {
        return detailsFilled;
    }

    /**
     * @param detailsFilled the detailsFilled to set
     */
    public void setDetailsFilled(boolean detailsFilled) {
        this.detailsFilled = detailsFilled;
    }

    /**
     * @return the deployedInStaging
     */
    public boolean isDeployedInStaging() {
        return deployedInStaging;
    }

    /**
     * @param deployedInStaging the deployedInStaging to set
     */
    public void setDeployedInStaging(boolean deployedInStaging) {
        this.deployedInStaging = deployedInStaging;
    }

    /**
     * @return the deployedInProd
     */
    public boolean isDeployedInProd() {
        return deployedInProd;
    }

    /**
     * @param deployedInProd the deployedInProd to set
     */
    public void setDeployedInProd(boolean deployedInProd) {
        this.deployedInProd = deployedInProd;
    }

    /**
     * @return the createdAt
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the lastEditedAt
     */
    public LocalDateTime getLastEditedAt() {
        return lastEditedAt;
    }

    /**
     * @param lastEditedAt the lastEditedAt to set
     */
    public void setLastEditedAt(LocalDateTime lastEditedAt) {
        this.lastEditedAt = lastEditedAt;
    }

    /**
     * @return the printingDate
     */
    public LocalDateTime getPrintingDate() {
        return printingDate;
    }

    /**
     * @param printingDate the printingDate to set
     */
    public void setPrintingDate(LocalDateTime printingDate) {
        this.printingDate = printingDate;
    }

    // /**
    //  * @return the formData
    //  */
    // public FormData getFormData() {
    //     return formData;
    // }

    // /**
    //  * @param formData the formData to set
    //  */
    // public void setFormData(FormData formData) {
    //     this.formData = formData;
    // }

}
