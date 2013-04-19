package handler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import datastore.GenericDAO;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */

@Path("/hello")
public class Hello {
    private static final Logger logger = LoggerFactory.getLogger(Hello.class);

    @Inject
    private GenericDAO<HelloWorld> msgDAO;

    @Path("/")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() {
        logger.info("Received hello - returning text!");
        return "Howdy World!!\n";
    }

    @Path("/error")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public HelloWorld getError() throws Exception {
        throw new Exception("Error!");
    }

    @Path("/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HelloWorld getJSON(@PathParam("name") String name) {
        logger.info("Received hello {} - returning json", name);
        if (!this.msgDAO.exists(name)) {
            logger.info("Creating new entry", name);
            HelloWorld world = new HelloWorld(name);
            this.msgDAO.save(world);
        }
        return this.msgDAO.get(name);
    }

}
