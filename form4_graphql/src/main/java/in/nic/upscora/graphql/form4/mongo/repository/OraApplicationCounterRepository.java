package in.nic.upscora.graphql.form4.mongo.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;

import in.nic.upscora.graphql.form4.mongo.entity.OraApplicationCounter;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;

@ApplicationScoped
public class OraApplicationCounterRepository implements PanacheMongoRepository<OraApplicationCounter> {

    public long incrementAndGet(String id) {
        MongoCollection<OraApplicationCounter> collection = mongoCollection();
        Bson filter = Filters.eq("_id", id);
        Bson update = Updates.inc("counter", 1L);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                .upsert(true)
                .returnDocument(ReturnDocument.AFTER);

        OraApplicationCounter result = collection.findOneAndUpdate(filter, update, options);
        if (result == null || result.getCounter() == null) {
            throw new IllegalStateException("Counter not found in DB result");
        }

        return result.getCounter();
    }
}
