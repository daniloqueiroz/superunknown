package handler;


/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class HelloWorld {

    private String message = "Hello World";
    private long timestamp = System.currentTimeMillis();

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }
}
