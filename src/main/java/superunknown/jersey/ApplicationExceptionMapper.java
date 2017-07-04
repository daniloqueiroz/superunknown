package superunknown.jersey;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import superunknown.ApplicationException;
import superunknown.Log;

public class ApplicationExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        return (exception instanceof ApplicationException)
                ? this.handleApplicationException((ApplicationException) exception)
                : this.handleException(exception);
    }

    protected Response handleApplicationException(ApplicationException exception) {
        Log.error("Application error processing request", exception);
        return Response.status(exception.getStatus()).build();
    }

    protected Response handleException(Exception exception) {
        Log.error("Unexpected error processing request", exception);
        return Response.serverError().build();
    }
}
