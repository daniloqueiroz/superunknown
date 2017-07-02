package superunknown.jersey;

import java.io.IOException;
import java.util.Optional;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;

/**
 * Checks if the response is an **Optional**.
 * 
 * If it's an Optional it either unwraps, if present, or return 404 if empty.
 */
public class OptionalResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) throws IOException {
        Object entity = response.getEntity();
        if (entity instanceof Optional) {
            Optional<?> opt = (Optional<?>) entity;
            if (opt.isPresent()) {
                response.setEntity(opt.get());
            } else {
                response.setStatus(Response.Status.NOT_FOUND.getStatusCode());
                response.setEntity(null);
            }
        }
    }
}