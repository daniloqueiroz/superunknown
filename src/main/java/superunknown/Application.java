package superunknown;

import static java.lang.String.format;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ExceptionMapper;

import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import superunknown.heartbeat.Heartbeat;
import superunknown.heartbeat.HeartbeatMonitor;
import superunknown.jersey.ApplicationExceptionMapper;
import superunknown.jersey.GsonProvider;
import superunknown.jersey.LogContextFilter;
import superunknown.jersey.MetricsFilter;
import superunknown.jersey.OptionalResponseFilter;
import superunknown.jersey.gson.GsonFactory;
import superunknown.resources.HeartbeatResource;
import superunknown.resources.InternalResource;

public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "0.0.0.0";
    private static final String DEFAULT_LOGLEVEL = "INFO";

    private int port = DEFAULT_PORT;
    private String host = DEFAULT_HOST;
    private String logLevel = DEFAULT_LOGLEVEL;
    private Set<Class<?>> registeredClasses = new LinkedHashSet<>();
    private Set<Object> registeredObjects = new LinkedHashSet<>();
    private Set<HeartbeatMonitor> monitors = new LinkedHashSet<>();
    private ExceptionMapper<?> mapper = new ApplicationExceptionMapper();

    public Application() {
        this.monitors.add(
                () -> Heartbeat
                        .builder()
                        .name("Superunknown")
                        .isHealthy(true)
                        .message("Superunknow is up and running")
                        .build());
    }

    public Application exceptionMapper(ExceptionMapper<?> mapper) {
        this.mapper = mapper;
        return this;
    }
    
    public Application port(int port) {
        this.port = port;
        return this;
    }

    public Application host(String host) {
        this.host = host;
        return this;
    }

    public Application logLevel(String level) {
        this.logLevel = level.toUpperCase();
        return this;
    }

    public Application register(HeartbeatMonitor monitor) {
        this.monitors.add(monitor);
        return this;
    }

    public Application register(Object resource) {
        this.registeredObjects.add(resource);
        return this;
    }

    public Application register(Class<?> resource) {
        this.registeredClasses.add(resource);
        return this;
    }

    public void start() {
        Log.configLog(this.logLevel);
        Log.context("application");
        ResourceConfig resourceConfig = createResourceConfig();

        try {
            URI base = new URI(format("http://%s:%s/", this.host, this.port));
            final Channel server = NettyHttpContainerProvider.createHttp2Server(base, resourceConfig, null);

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    server.close();
                }
            }));

            LOG.info("Successfully Started.");
            System.out.printf("Application started - %s\nStop the application using 'CTRL+C'\n", base.toString());

            Thread.currentThread().join();
        } catch (Exception ex) {
            LOG.error("Application error", ex);
        }
    }

    private ResourceConfig createResourceConfig() {
        ResourceConfig resourceConfig = new ResourceConfig();
        this.registerInternals(resourceConfig);
        this.registerExternals(resourceConfig);
        return resourceConfig;
    }

    private void registerInternals(ResourceConfig resourceConfig) {
        resourceConfig.register(new InternalResource(new HeartbeatResource(this.monitors)));
        resourceConfig.register(this.mapper);
        resourceConfig.register(LogContextFilter.class, 1);
        resourceConfig.register(new MetricsFilter(), 2);
        resourceConfig.register(OptionalResponseFilter.class, 1000);
        resourceConfig.register(new GsonProvider(GsonFactory::defaultGson), 10);
    }

    private void registerExternals(ResourceConfig resourceConfig) {
        this.registeredClasses.forEach(resourceConfig::registerClasses);
        this.registeredObjects.forEach(resourceConfig::registerInstances);
    }

    // For demostration purposes

    public static void main(String[] args) {
        new Application().register(ExampleResource.class).start();
    }

    @Path("/hello")
    public static class ExampleResource {
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Optional<Message> getHello() {
            return Optional.of(new Message("Hello there"));
        }

        public static class Message {
            public String message;

            public Message(String msg) {
                this.message = msg;
            }
        }
    }
}
