package datastore;

/**
 * Indicates that an Object was not found by a DAO
 * 
 * @see GenericDAO#get(Object)
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class ObjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5449468828324393156L;

    public ObjectNotFoundException() {
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(Throwable cause) {
        super(cause);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
