package com.github.danisimov.mockochino.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.danisimov.mockochino.service.Const;
import com.github.danisimov.mockochino.service.Settings;
import com.github.danisimov.mockochino.service.SettingsManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

/**
 * Root resource (exposed at "settings/" path)
 */
@Path("settings/")
public class SettingsHandler {

    private final SettingsManager settingsManager = SettingsManager.getInstance();

    /**
     * Method handling HTTP requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return Response that will be returned as a json.
     */
    @POST
    @Path("init")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response init() {
        UUID uuid = UUID.randomUUID();
        if (settingsManager.add(uuid)) {
            return Response.status(200).entity(new ObjectMapper().createObjectNode()
                    .put(Const.RESULT, true)
                    .put(Const.UUID, uuid.toString())).build();
        } else {
            return Response.status(500).entity(new ObjectMapper().createObjectNode()
                    .put(Const.RESULT, false)
                    .put(Const.MESSAGE, Const.INSERTING_DATA_ERROR)).build();
        }
    }

    @GET
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        return Response.status(200).entity(new ObjectMapper().createObjectNode()
                .put(Const.RESULT, true)
                .set(Const.DATA, settingsManager.get())).build();
    }

    @GET
    @Path("{uuid}")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByUUID(@PathParam(Const.UUID) UUID uuid) {
        Settings settings = settingsManager.getByUUID(uuid);

        if (settings != null) {
            return Response.status(200).entity(new ObjectMapper().createObjectNode()
                    .put(Const.RESULT, true)
                    .set(Const.DATA, new ObjectMapper().createArrayNode().add(new ObjectMapper().createObjectNode()
                            .put(Const.UUID, String.valueOf(settings.getUuid()))
                            .put(Const.DELAY, settings.getDelay())
                            .put(Const.CODE, settings.getCode())
                            .put(Const.TOKEN, settings.getToken())
                            .set(Const.RESPONSE, settings.getResponse())))).build();
        } else {
            return Response.status(404).entity(new ObjectMapper().createObjectNode()
                    .put(Const.RESULT, false)
                    .put(Const.MESSAGE, Const.buildNoSuchSettingsMessage(uuid))).build();
        }
    }

    @POST
    @Path("{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam(Const.UUID) UUID uuid, JsonNode json) {
        Settings settings = settingsManager.getByUUID(uuid);

        if (settings != null) {
            if (json.has(Const.DELAY) && json.get(Const.DELAY).isInt())
                settings.setDelay(json.get(Const.DELAY).asInt());
            if (json.has(Const.CODE) && json.get(Const.CODE).isInt()) settings.setCode(json.get(Const.CODE).asInt());
            if (json.has(Const.TOKEN) && json.get(Const.TOKEN).isTextual())
                settings.setToken(json.get(Const.TOKEN).asText());
            if (json.has(Const.RESPONSE) && json.get(Const.RESPONSE).isObject())
                settings.setResponse(json.get(Const.RESPONSE));

            if (settingsManager.update(settings)) {
                return Response.status(200).entity(new ObjectMapper().createObjectNode()
                        .put(Const.RESULT, true)).build();
            } else {
                return Response.status(500).entity(new ObjectMapper().createObjectNode()
                        .put(Const.RESULT, false)
                        .put(Const.MESSAGE, Const.UPDATING_DATA_ERROR)).build();
            }
        } else {
            return Response.status(404).entity(new ObjectMapper().createObjectNode()
                    .put(Const.RESULT, false)
                    .put(Const.MESSAGE, Const.buildNoSuchSettingsMessage(uuid))).build();
        }
    }

    @POST
    @Path("reset/{uuid}")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reset(@PathParam(Const.UUID) UUID uuid) {
        Settings settings = settingsManager.getByUUID(uuid);

        if (settings != null) {
            if (settingsManager.update(settings.setDefaultSettings())) {
                return Response.status(200).entity(new ObjectMapper().createObjectNode()
                        .put(Const.RESULT, true)).build();
            } else {
                return Response.status(500).entity(new ObjectMapper().createObjectNode()
                        .put(Const.RESULT, false)
                        .put(Const.MESSAGE, Const.UPDATING_DATA_ERROR)).build();
            }
        } else {
            return Response.status(404).entity(new ObjectMapper().createObjectNode()
                    .put(Const.RESULT, false)
                    .put(Const.MESSAGE, Const.buildNoSuchSettingsMessage(uuid))).build();
        }
    }

    @DELETE
    @Path("{uuid}")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteByUUID(@PathParam(Const.UUID) UUID uuid) {
        return Response.status(200).entity(new ObjectMapper().createObjectNode()
                .put(Const.RESULT, settingsManager.deleteByUUID(uuid))).build();
    }
}
