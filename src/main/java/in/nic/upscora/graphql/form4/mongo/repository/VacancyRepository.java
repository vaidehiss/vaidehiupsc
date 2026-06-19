package in.nic.upscora.graphql.form4.mongo.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

import in.nic.upscora.graphql.form4.mongo.entity.vacancy.Vacancy;


@ApplicationScoped
public class VacancyRepository implements PanacheMongoRepository<Vacancy> {

    public Vacancy findByVacancyId(String vacancyId) {
        return find("vacancyId", vacancyId).firstResult();
    }

    public Vacancy findByVacancyNumber(String vacancyNumber) {
        return find("vacancyNumber", vacancyNumber).firstResult();
    }

    public Optional<Vacancy> findLatestByVacancyId(String vacancyId) {
        return find("vacancyId", Sort.by("lastEditedAt", Sort.Direction.Descending)
                        .and("createdAt", Sort.Direction.Descending), vacancyId)
                .firstResultOptional();
    }

}
