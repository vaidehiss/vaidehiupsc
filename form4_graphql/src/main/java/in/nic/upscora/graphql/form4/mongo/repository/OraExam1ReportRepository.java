package in.nic.upscora.graphql.form4.mongo.repository;

import in.nic.upscora.graphql.form4.mongo.entity.application.OraExam1Report;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OraExam1ReportRepository implements PanacheMongoRepository<OraExam1Report> {

    public OraExam1Report findByVacancyId(String vacancyId) {
        return find("_id", vacancyId).firstResult();
    }
}
