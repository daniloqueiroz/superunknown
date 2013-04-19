package server;

import handler.Hello;
import handler.HelloWorld;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.TypeLiteral;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import datastore.GenericDAO;
import datastore.jdo.JDOGenericDAO;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class JerseyGuiceModule extends JerseyServletModule {
    /**
     * Binds to Guice the Jersey Resources
     */
    private void bindJerseyResources() {
        this.bind(Hello.class);
    }

    /**
     * Binds other dependencies to Guice
     */
    private void bindDependencies() {
        this.bind(new TypeLiteral<GenericDAO<HelloWorld>>() {
        }).toInstance(new JDOGenericDAO<HelloWorld>(HelloWorld.class));
    }

    /**
     * Bind Jersey's dependencies, such as jackson and others
     */
    private void bindJerseyDependencies() {
        this.bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
        this.bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);
    }

    @Override
    protected void configureServlets() {
        this.bindJerseyResources();
        this.bindJerseyDependencies();
        this.bindDependencies();

        // Route all requests through GuiceContainer
        this.serve("/*").with(GuiceContainer.class);
    }
}
