package superunknown.resources;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/_")
@Produces(MediaType.APPLICATION_JSON)
public class InternalResource {

    private HeartbeatResource heartbeat;

    public InternalResource(HeartbeatResource heartbeat) {
        this.heartbeat = heartbeat;
    }

    @Path("/heartbeat")
    public HeartbeatResource heartbeatResource() {
        return this.heartbeat;
    }
}
