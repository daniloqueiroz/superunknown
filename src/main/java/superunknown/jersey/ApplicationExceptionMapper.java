package superunknown.jersey;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import superunknown.ApplicationException;
import superunknown.Log;

public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {

    @Override
    public Response toResponse(ApplicationException exception) {
        Log.error("Error processing request", exception);
        return Response.status(exception.getStatus()).build();
    }
}
