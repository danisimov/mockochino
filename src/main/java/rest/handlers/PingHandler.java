package rest.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import rest.service.Const;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
