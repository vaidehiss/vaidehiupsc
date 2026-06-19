package in.nic.upscora.graphql.form4.client;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import in.nic.upscora.graphql.form4.client.model.NotificationRequest;

@Path("/api/v2/notification")
@RegisterRestClient(configKey = "notification-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface NotificationClient {

    @POST
    void sendNotification(
            @HeaderParam("x-notification-correlation-id") String correlationId,
            @HeaderParam("accept-language") String language,
            NotificationRequest request
    );
}
