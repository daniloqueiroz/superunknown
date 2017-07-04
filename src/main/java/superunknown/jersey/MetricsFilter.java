package superunknown.jersey;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import superunknown.Log;

/**
 * Records information about the request processing time and status.
 */
public class MetricsFilter implements ContainerResponseFilter, ContainerRequestFilter {

    private Map<ContainerRequestContext, Instant> timers = new HashMap<>();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        this.timers.put(requestContext, Instant.now());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) throws IOException {
        Instant start = this.timers.remove(requestContext);
        long ms = Duration.between(start, Instant.now()).toMillis();
        Log.info("Status: {}; Duration: {} ms", response.getStatus(), ms);
        // TODO send to metrics/abacus
    }

}