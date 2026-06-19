package in.nic.upscora.graphql.form4.mongo.repository;

import in.nic.upscora.graphql.form4.mongo.entity.application.OraApplicationAudit;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OraApplicationAuditRepository implements PanacheMongoRepository<OraApplicationAudit> {

    public OraApplicationAudit findByApplicantUrnAndVacancy(String applicantUrn, String vacancyId) {
        return find("applicant_urn =?1 and vacancyId = ?2", applicantUrn, vacancyId).firstResult();
    }
}
