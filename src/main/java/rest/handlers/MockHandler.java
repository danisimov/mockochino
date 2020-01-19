package rest.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import rest.service.Const;
import rest.service.Settings;
import rest.service.SettingsManager;
import rest.service.StorageManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * Root resource (exposed at "mock" path)
 */
@Path("mock/{uuid}")
public class MockHandler {

    /**
     * Methods handling HTTP requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return Response that will be returned as a json.
     */
    @GET
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam(Const.UUID) UUID uuid, String request, @HeaderParam("Authorization") String token) throws InterruptedException {
        return processRequest(uuid, request, true, token);
    }

    @POST
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@PathParam(Const.UUID) UUID uuid, String request, @HeaderParam("Authorization") String token) throws InterruptedException {
        return processRequest(uuid, request, true, token);
    }

    @PUT
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@PathParam(Const.UUID) UUID uuid, String request, @HeaderParam("Authorization") String token) throws InterruptedException {
        return processRequest(uuid, request, true, token);
    }

    @DELETE
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam(Const.UUID) UUID uuid, String request, @HeaderParam("Authorization") String token) throws InterruptedException {
        return processRequest(uuid, request, true, token);
    }

    @PATCH
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@PathParam(Const.UUID) UUID uuid, String request, @HeaderParam("Authorization") String token) throws InterruptedException {
        return processRequest(uuid, request, true, token);
    }

    @HEAD
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response head(@PathParam(Const.UUID) UUID uuid, String request, @HeaderParam("Authorization") String token) throws InterruptedException {
        return processRequest(uuid, request, false, token);
    }

    private Response processRequest(UUID uuid, String request, boolean withResponseBody, String token) throws InterruptedException {
        Settings settings = SettingsManager.getInstance().getByUUID(uuid);

        if (settings != null) {
            if (!settings.getToken().isEmpty() && (token == null || !token.equals(settings.getToken()))) {
                return produceResponse(401, new ObjectMapper().createObjectNode()
                        .put(Const.RESULT, false)
                        .put(Const.MESSAGE, "Authorization failed."));
            }

            if (settings.getDelay() > 0) Thread.sleep(settings.getDelay());

            if (StorageManager.getInstance().add(uuid, request)) {
                if (withResponseBody) return produceResponse(settings.getCode(), settings.getResponse());
                else return produceResponse(settings.getCode(), null);
            } else {
                return produceResponse(500, new ObjectMapper().createObjectNode()
                        .put(Const.RESULT, false)
                        .put(Const.MESSAGE, "Something went wrong during request processing."));
            }
        } else {
            return produceResponse(404, new ObjectMapper().createObjectNode()
                    .put(Const.RESULT, false)
                    .put(Const.MESSAGE, Const.buildNoSuchSettingsMessage(uuid)));
        }
    }

    private Response produceResponse(int code, JsonNode response) {
        return Response.status(code).entity(response).build();
    }
}
