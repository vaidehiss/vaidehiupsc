package in.nic.upscora.graphql.form4.client;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import com.fasterxml.jackson.databind.JsonNode;

import in.nic.upscora.graphql.form4.client.model.GraphQLRequest;

@Path("/graphql")
@RegisterRestClient(configKey = "caf-master")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CAFMasterGraphQLClient {
    
    @POST
    JsonNode executeQuery(GraphQLRequest request);
}
