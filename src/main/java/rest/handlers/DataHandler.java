package rest.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import rest.service.Const;
import rest.service.StorageManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * Root resource (exposed at "request/" path)
 */
@Path("request/")
public class DataHandler {

    /**
     * Method handling HTTP requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return Response that will be returned as a json.
     */
    @GET
    @Path("{uuid}")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByUUID(@PathParam(Const.UUID) UUID uuid) {
        return Response.status(200).entity(new ObjectMapper().createObjectNode()
                .put(Const.RESULT, true)
                .set(Const.DATA, StorageManager.getInstance().getByUUID(uuid))).build();
    }

    @GET
    @Path("count")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response count() {
        return Response.status(200).entity(new ObjectMapper().createObjectNode()
                .put(Const.RESULT, true)
                .put(Const.AMOUNT, StorageManager.getInstance().count())).build();
    }

    @GET
    @Path("count/{uuid}")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response countByUUID(@PathParam(Const.UUID) UUID uuid) {
        return Response.status(200).entity(new ObjectMapper().createObjectNode()
                .put(Const.RESULT, true)
                .put(Const.AMOUNT, StorageManager.getInstance().countByUUID(uuid))).build();
    }

    @DELETE
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete() {
        return Response.status(200).entity(new ObjectMapper().createObjectNode()
                .put(Const.RESULT, StorageManager.getInstance().delete())).build();
    }

    @DELETE
    @Path("{uuid}")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteByUUID(@PathParam(Const.UUID) UUID uuid) {
        return Response.status(200).entity(new ObjectMapper().createObjectNode()
                .put(Const.RESULT, StorageManager.getInstance().deleteByUUID(uuid))).build();
    }
}
