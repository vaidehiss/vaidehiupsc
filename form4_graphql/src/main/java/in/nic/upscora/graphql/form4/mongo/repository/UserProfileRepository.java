package in.nic.upscora.graphql.form4.mongo.repository;

import in.nic.upscora.graphql.form4.mongo.entity.userprofile.UserProfile;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserProfileRepository implements PanacheMongoRepository<UserProfile> {

    public UserProfile findById(Long id) {
        return find("_id", id).firstResult();
    }

    public UserProfile findByMobileNo(Long mobileNo) {
        return find("mobileNo", mobileNo).firstResult();
    }



}
