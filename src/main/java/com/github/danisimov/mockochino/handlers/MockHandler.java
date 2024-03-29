package com.github.danisimov.mockochino.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.danisimov.mockochino.service.Const;
import com.github.danisimov.mockochino.service.Settings;
import com.github.danisimov.mockochino.service.SettingsManager;
import com.github.danisimov.mockochino.service.StorageManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@PathParam(Const.UUID) UUID uuid, String request, @HeaderParam("Authorization") String token) throws InterruptedException {
        return processRequest(uuid, request, true, token);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@PathParam(Const.UUID) UUID uuid, String request, @HeaderParam("Authorization") String token) throws InterruptedException {
        return processRequest(uuid, request, true, token);
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@PathParam(Const.UUID) UUID uuid, String request, @HeaderParam("Authorization") String token) throws InterruptedException {
        return processRequest(uuid, request, true, token);
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
