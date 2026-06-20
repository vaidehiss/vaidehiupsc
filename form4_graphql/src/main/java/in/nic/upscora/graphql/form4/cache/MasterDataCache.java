package in.nic.upscora.graphql.form4.cache;

import com.fasterxml.jackson.databind.JsonNode;
import in.nic.upscora.graphql.form4.client.CAFMasterGraphQLClient;
import in.nic.upscora.graphql.form4.client.model.GraphQLRequest;
import in.nic.upscora.graphql.form4.exception.AppException;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * Enterprise Master Data Cache Service.
 * Implements a strategy-based parsing engine to load and normalize master data.
 */
@Slf4j
@ApplicationScoped
public class MasterDataCache {

    private final CAFMasterGraphQLClient cafMasterGraphQLClient;
    private final Map<String, Map<String, String>> masterData;

    @Inject
    public MasterDataCache(
            @RestClient CAFMasterGraphQLClient cafMasterGraphQLClient) {
        this.cafMasterGraphQLClient = cafMasterGraphQLClient;
        this.masterData = new ConcurrentHashMap<>();
    }

    // Extraction Strategies
    private static final List<String> ID_PROBES = List.of(
            "id",
            "examCode",
            "ageRelax",
            "relaxCode",
            "divCode",
            "registrationCode",
            "centerCode",
            "sid",
            "wid");

    private static final List<String> NAME_PROBES = List.of(
            "nm",
            "name",
            "nameEn",
            "dtNm",
            "examName",
            "boardName",
            "categoryName",
            "relaxName",
            "divName",
            "registrationName",
            "centerName");

    private static final Set<String> RETAINED_BUCKETS = Set.of(
            MasterDataConstants.GENDERS,
            MasterDataConstants.COMMUNITIES,
            MasterDataConstants.SID_CACHE,
            MasterDataConstants.WID_CACHE);

    void onStart(@Observes StartupEvent ev) {
        log.info("🚀 Initializing Master Data Cache Engine...");
        try {
            String query = loadQuery();
            GraphQLRequest request = GraphQLRequest.builder()
                    .query(query)
                    .build();

            log.info("Fetching master data from CAF Master Service...");
            JsonNode response = cafMasterGraphQLClient.executeQuery(request);

            if (isValidResponse(response)) {
                JsonNode combinedData = response
                        .get("data")
                        .get("getCombinedMasterDistrictData");
                processRawData(combinedData);
                printCacheSummary();
            } else {
                log.error(
                        "Failed to parse master data response. Check the GraphQL query or endpoint.");
            }
        } catch (Exception e) {
            log.error(
                    "❌ Critical Failure: Could not load Master Data Cache during startup.",
                    e);
        }
    }

    private String loadQuery() throws IOException {
        try (
                InputStream is = getClass().getResourceAsStream(
                        "/master-data.graphql")) {
            if (is == null)
                throw new AppException(
                        "master-data.graphql not found");
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private boolean isValidResponse(JsonNode response) {
        return (response != null &&
                response.has("data") &&
                response.get("data").has("getCombinedMasterDistrictData"));
    }

    private void processRawData(JsonNode combinedData) {
        Set<Map.Entry<String, JsonNode>> fields = combinedData.properties();
        for (Entry<String, JsonNode> field : fields) {
            String collectionName = field.getKey();
            JsonNode elements = field.getValue();

            if (elements.isArray()) {
                processCollection(collectionName, elements);
            }
        }
    }

    private void processCollection(String collectionName, JsonNode elements) {
        boolean retainBucket = shouldRetainBucket(collectionName);

        if (retainBucket) {
            Map<String, String> bucket = new HashMap<>();
            for (JsonNode element : elements) {
                String id = probeField(element, ID_PROBES);
                String name = probeField(element, NAME_PROBES);
                if (id != null && name != null) {
                    bucket.put(id, name);
                }
                handleNestedSubsets(element, collectionName);
            }

            if (!bucket.isEmpty()) {
                masterData.merge(collectionName, bucket, (oldMap, newMap) -> {
                    oldMap.putAll(newMap);
                    return oldMap;
                });
            }
        }

        applyLogicalAliases(collectionName, elements);
    }

    private boolean shouldRetainBucket(String collectionName) {
        return (collectionName != null && RETAINED_BUCKETS.contains(collectionName));
    }

    private void handleNestedSubsets(JsonNode element, String collectionName) {
        if (MasterDataConstants.DISABILITY_TYPES.equals(collectionName)) {
            extractNested(
                    element,
                    "sdt",
                    MasterDataConstants.SID_CACHE,
                    "sid",
                    "nm");
            extractNested(
                    element,
                    "wec",
                    MasterDataConstants.WID_CACHE,
                    "wid",
                    "nm");
        }
    }

    private void applyLogicalAliases(String collectionName, JsonNode elements) {
        // FLAT MAP SPECIAL: disabilities -> sidcache
        if (MasterDataConstants.DISABILITIES.equals(collectionName)) {
            populateBucket(MasterDataConstants.SID_CACHE, elements);
        }

        // FLAT MAP SPECIAL: writingExtremityOptions -> widcache
        if ("writingExtremityOptions".equals(collectionName)) {
            populateBucket(MasterDataConstants.WID_CACHE, elements);
        }
    }

    private void extractNested(
            JsonNode parent,
            String fieldName,
            String targetBucket,
            String idKey,
            String nameKey) {
        if (parent.has(fieldName) && parent.get(fieldName).isArray()) {
            Map<String, String> bucket = masterData.computeIfAbsent(
                    targetBucket,
                    k -> new ConcurrentHashMap<>());
            for (JsonNode subNode : parent.get(fieldName)) {
                String id = subNode.has(idKey)
                        ? subNode.get(idKey).asText()
                        : null;
                String name = subNode.has(nameKey)
                        ? subNode.get(nameKey).asText()
                        : null;
                if (id != null && name != null) {
                    bucket.put(id, name);
                }
            }
        }
    }

    private void populateBucket(String bucketName, JsonNode elements) {
        Map<String, String> bucket = masterData.computeIfAbsent(bucketName, k -> new ConcurrentHashMap<>());
        for (JsonNode element : elements) {
            String id = probeField(element, ID_PROBES);
            String name = probeField(element, NAME_PROBES);
            if (id != null && name != null) {
                bucket.put(id, name);
            }
        }
    }

    private String probeField(JsonNode node, List<String> candidates) {
        for (String field : candidates) {
            if (node.has(field) && !node.get(field).isNull()) {
                return node.get(field).asText();
            }
        }
        return null;
    }

    public String lookup(String collection, String id) {
        if (id == null || collection == null)
            return null;
        Map<String, String> lookupMap = masterData.get(collection);
        return (lookupMap != null) ? lookupMap.get(id) : null;
    }

    private void printCacheSummary() {
        log.info("======= Master Data Cache Engine Summary =======");
        masterData.forEach((collection, map) -> log.info("Bucket: [{}], Size: {}", collection, map.size()));
        log.info(
                "Verification: lookup('sidcache', '9') -> {}",
                lookup(MasterDataConstants.SID_CACHE, "9"));
        log.info("===============================================");
    }
}
