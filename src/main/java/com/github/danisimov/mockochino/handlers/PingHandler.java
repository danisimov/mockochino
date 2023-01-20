package com.github.danisimov.mockochino.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.danisimov.mockochino.service.Const;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Root resource (exposed at root path)
 */
@Path("")
public class PingHandler {

    /**
     * Methods handling HTTP requests with json. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return Response that will be returned as a json.
     */
    @GET
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ping() {
        return Response.status(200).entity(new ObjectMapper().createObjectNode()
                .put(Const.RESULT, true)).build();
    }
}
