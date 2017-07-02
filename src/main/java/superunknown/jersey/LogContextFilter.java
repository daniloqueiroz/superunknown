package superunknown.jersey;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import superunknown.Log;

public class LogContextFilter implements ContainerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(LogContextFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Log.context();
        this.logRequest(requestContext);
    }

    private void logRequest(ContainerRequestContext req) {
        LOG.info("{} {} \"{}\"", req.getMethod(), req.getUriInfo().getRequestUri(), req.getHeaderString("User-Agent"));
    }

}
