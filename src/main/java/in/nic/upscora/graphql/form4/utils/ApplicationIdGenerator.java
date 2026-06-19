package in.nic.upscora.graphql.form4.utils;

import in.nic.upscora.graphql.form4.mongo.repository.OraApplicationCounterRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@ApplicationScoped
public class ApplicationIdGenerator {

    private static final String DEFAULT_COUNTER_ID = "EXAM_APPLICATION_ID";

    @Inject
    OraApplicationCounterRepository counterRepository;

    @ConfigProperty(name = "application.id.prefix", defaultValue = "")
    String applicationIdPrefix;

    @ConfigProperty(
        name = "application.id.counter-id",
        defaultValue = DEFAULT_COUNTER_ID
    )
    String counterId;

    public synchronized long generateId() {
        try {
            long counter = counterRepository.incrementAndGet(counterId);
            String numericPart = applicationIdPrefix + counter;
            int checksum = numericPart.chars().sum() % 10;
            String applicationId = numericPart + checksum;

            return Long.parseLong(applicationId);
        } catch (Exception e) {
            log.error("Failed to generate application ID", e);
            throw new RuntimeException("Failed to generate application ID", e);
        }
    }

    /**
     * Generate batch of IDs
     */
    public synchronized long[] generateBatch(int count) {
        if (count <= 0 || count > 1000) {
            throw new IllegalArgumentException(
                "Count must be between 1 and 1000"
            );
        }

        long[] ids = new long[count];
        for (int i = 0; i < count; i++) {
            ids[i] = generateId();
        }
        return ids;
    }
}
