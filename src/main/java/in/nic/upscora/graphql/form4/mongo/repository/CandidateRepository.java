package in.nic.upscora.graphql.form4.mongo.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import in.nic.upscora.graphql.form4.mongo.entity.caf.CandidateProfile;

@ApplicationScoped
public class CandidateRepository implements PanacheMongoRepository<CandidateProfile> {

    public CandidateProfile findById(Long id) {
        return find("_id", id).firstResult();
    }

    public CandidateProfile findByIdStr(String id) {
        return find("_id", id).firstResult();
    }


}
