package in.nic.upscora.graphql.form4.mongo.repository;

import java.util.List;

import in.nic.upscora.graphql.form4.mongo.entity.application.OraApplication;
import in.nic.upscora.graphql.form4.service.OraExam1ReportService;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class OraApplicationRepository implements PanacheMongoRepository<OraApplication> {

    private final OraExam1ReportService reportService;

    public OraApplicationRepository(OraExam1ReportService reportService) {
        this.reportService = reportService;
    }

    public List<OraApplication> findAllApplicants() {
        return listAll();
    }

    public OraApplication findByURN(String urn) {
        return find("applicant_urn", urn).firstResult();
    }

    public List<OraApplication> findAllSubmittedByURN(String urn) {
        return find("{'applicant_urn': ?1, 'application_info.status': 'SUBMITTED'}", urn).list();
    }

    public OraApplication findByURNAndVacancy(String urn, String vacancyId) {
        return find("applicant_urn =?1 and vacancyId = ?2", urn, vacancyId).firstResult();
    }

    public List<OraApplication> findByVacancyId(String vacancyId) {
        return find("vacancyId", vacancyId).list();
    }

    public OraApplication findByURNAndRecruitmentCode(String urn, String recruitmentCode) {
        return find("applicant_urn = ?1 and application_info.recruitmentCode = ?2", urn, recruitmentCode).firstResult();
    }

    public List<OraApplication> findByRecruitmentCode(String recruitmentCode) {
        return find("application_info.recruitmentCode", recruitmentCode).list();
    }

    public List<OraApplication> findSubmittedByVacancyIds(List<String> vacancyIds) {
        return find("{'vacancyId': { $in: ?1 }, 'application_info.status': 'SUBMITTED', 'testing': { $ne: true }}",
                vacancyIds).list();
    }

    public List<OraApplication> findSubmittedByRecruitmentCode(String recruitmentCode) {
        return find(
                "{'application_info.recruitmentCode': ?1, 'application_info.status': 'SUBMITTED', 'testing': { $ne: true }}",
                recruitmentCode).list();
    }

    public OraApplication findById(Long id) {
        return find("_id", id).firstResult();
    }

    @Override
    public void persist(OraApplication entity) {
        PanacheMongoRepository.super.persist(entity);
        reportService.refreshFromApplication(entity);
    }

    @Override
    public void update(OraApplication entity) {
        PanacheMongoRepository.super.update(entity);
        reportService.refreshFromApplication(entity);
    }

    @Override
    public void delete(OraApplication entity) {
        PanacheMongoRepository.super.delete(entity);
        reportService.refreshFromApplication(entity);
    }

}
