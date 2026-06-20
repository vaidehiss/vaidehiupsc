package in.nic.upscora.graphql.form4.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.client.api.ClientMultipartForm;

@RegisterRestClient(configKey = "ngrp-api")
@Path("/")
public interface NgrpDocumentClient {

    @GET
    @Path("/ora_api/s3/download")
    @Produces(MediaType.WILDCARD)
    Response downloadCandidateDocument(
            @QueryParam("filename") String filename,
        //     @HeaderParam("accept") String accept,
            @HeaderParam("type") String type,
            @HeaderParam("type_of_document") String typeOfDocument,
            @HeaderParam("applicant_id") String applicantId,
            @HeaderParam("lang") String lang
    );

    @POST
    @Path("/ora_api/s3/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    Response uploadCandidateDocument(
            @HeaderParam("accept") String accept,
            @HeaderParam("type") String type,
            @HeaderParam("type_of_document") String typeOfDocument,
            @HeaderParam("applicant_id") String applicantId,
            @HeaderParam("post_id") String postId,
            @HeaderParam("lang") String lang,
            ClientMultipartForm form
    );

    @POST
    @Path("/ora_api/s3/usertorecruitment")
    @Produces(MediaType.APPLICATION_JSON)
    Response copyUserDocsToRecruitment(
            @HeaderParam("applicant_id") String applicantId,
            @HeaderParam("post_id") String postId
    );

    @DELETE
    @Path("/ora_api/s3/clearcaf")
    @Produces(MediaType.APPLICATION_JSON)
    Response clearCafDocsFromRecruitment(
            @HeaderParam("applicant_id") String applicantId,
            @HeaderParam("post_id") String postId
    );
}
