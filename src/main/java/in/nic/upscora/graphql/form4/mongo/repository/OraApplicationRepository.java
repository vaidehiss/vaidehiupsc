package in.nic.upscora.graphql.form4.mongo.repository;

import java.util.List;

import in.nic.upscora.graphql.form4.mongo.entity.application.OraApplication;
import in.nic.upscora.graphql.form4.service.OraExam1ReportService;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class OraApplicationRepository implements PanacheMongoRepository<OraApplication> {

    @Inject
    OraExam1ReportService reportService;
    
    
    public List<OraApplication> findAllApplicants() {
        return listAll();
    }
    
    
    public OraApplication findByURN(String URN) {
        return find("applicant_urn", URN).firstResult();
    }

    public List<OraApplication> findAllSubmittedByURN(String URN) {
        return find("{'applicant_urn': ?1, 'application_info.status': 'SUBMITTED'}", URN).list();
    }

    public OraApplication findByURNAndVacancy(String URN,String vacancyId) {
        return find("applicant_urn =?1 and vacancyId = ?2", URN,vacancyId).firstResult();
    }

    public List<OraApplication> findByVacancyId(String vacancyId) {
        return find("vacancyId", vacancyId).list();
    }

    public OraApplication findByURNAndRecruitmentCode(String URN, String recruitmentCode) {
        return find("applicant_urn = ?1 and application_info.recruitmentCode = ?2", URN, recruitmentCode).firstResult();
    }

    public List<OraApplication> findByRecruitmentCode(String recruitmentCode) {
        return find("application_info.recruitmentCode", recruitmentCode).list();
    }

    public List<OraApplication> findSubmittedByVacancyIds(List<String> vacancyIds) {
        return find("{'vacancyId': { $in: ?1 }, 'application_info.status': 'SUBMITTED', 'testing': { $ne: true }}", vacancyIds).list();
    }

    public List<OraApplication> findSubmittedByRecruitmentCode(String recruitmentCode) {
        return find("{'application_info.recruitmentCode': ?1, 'application_info.status': 'SUBMITTED', 'testing': { $ne: true }}", recruitmentCode).list();
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
