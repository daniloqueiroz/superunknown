package superunknown.jersey;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

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
        Optional<Instant> start = Optional.ofNullable(this.timers.remove(requestContext));
        AtomicLong ms = new AtomicLong(-1);
        start.ifPresent(s -> ms.set(Duration.between(s, Instant.now()).toMillis()));

        Log.info("Status: {}; Duration: {} ms", response.getStatus(), ms.get());
        // TODO send to metrics/abacus
    }

}