package handler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */

@Path("/hello")
public class Hello {
    @Path("/text") @GET
    @Produces("text/plain")
    public String get() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Received hellotext get!");

        return "Howdy World!\n";
    }

    @Path("/json") @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HelloWorld getJSON() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Received hellojson get!");
        return new HelloWorld();
    }

}
