package handler;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
@PersistenceCapable
public class HelloWorld {

    @PrimaryKey
    private String name;
    @Persistent
    private String message = "Hello World";

    public HelloWorld(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
