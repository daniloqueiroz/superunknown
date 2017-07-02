package superunknown.resources;

import static java.util.stream.Collectors.toList;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import superunknown.Log;
import superunknown.heartbeat.Heartbeat;
import superunknown.heartbeat.HeartbeatMonitor;

public class HeartbeatResource {

    private Collection<HeartbeatMonitor> monitors;

    public HeartbeatResource(Collection<HeartbeatMonitor> monitors) {
        this.monitors = monitors;
    }

    @GET
    public Response checkHeartbeat() {
        Collection<Heartbeat> heartbeats = this.monitors.stream().map(HeartbeatMonitor::isAlive).collect(toList());
        Status status = heartbeats.stream().allMatch(Heartbeat::isHealthy)? Status.OK: Status.SERVICE_UNAVAILABLE;
        Log.info("Heartbeat check: {}",  status);
        return Response.status(status).entity(heartbeats).build();
    }
}
