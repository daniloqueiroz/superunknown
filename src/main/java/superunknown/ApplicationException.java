package superunknown;

import javax.ws.rs.core.Response.Status;

import lombok.Getter;

public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = -8635530514214207159L;

    @Getter
    private final Status status;

    public ApplicationException(Status status, String message) {
        super(message);
        this.status = status;
    }

    public ApplicationException(Status status, Throwable cause) {
        super(cause);
        this.status = status;
    }

    public ApplicationException(Status status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
