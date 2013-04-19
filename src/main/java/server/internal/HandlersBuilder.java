package server.internal;

import static java.io.File.separatorChar;
import static server.JerseyHandlerHelpers.getDefaultServletClass;
import static server.JerseyHandlerHelpers.getErrorHandler;
import static server.JerseyHandlerHelpers.getFilters;

import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.Filter;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.gzip.GzipHandler;

import server.Configuration;
import server.JerseyGuiceModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class HandlersBuilder {

    private Configuration config;

    public HandlersBuilder(Configuration config) {
        this.config = config;
    }

    public Handler createHandler() {
        RequestLogHandler handler = new RequestLogHandler();
        handler.setRequestLog(this.createRequestLog());
        handler.setHandler(this.getHandlersList());
        return handler;
    }

    private RequestLog createRequestLog() {
        String logFilename = this.config.getRequestLogFolder() + separatorChar
                + "jetty-yyyy_mm_dd.request.log";
        NCSARequestLog requestLog = new NCSARequestLog(logFilename);
        requestLog.setRetainDays(90);
        requestLog.setAppend(true);
        requestLog.setExtended(false);
        return requestLog;
    }

    private Handler getHandlersList() {
        HandlerList list = new HandlerList();
        for (Handler h : this.createStaticHandlers()) {
            list.addHandler(h);
        }
        list.addHandler(this.createGuiceServletHandler());
        return list;
    }

    private Collection<Handler> createStaticHandlers() {
        Collection<Handler> staticHandlers = new LinkedList<>();
        for (String name : this.config.getStaticResources()) {
            ResourceHandler resource = new ResourceHandler();
            resource.setDirectoriesListed(false);
            resource.setResourceBase(name);
            ContextHandler context = new ContextHandler();
            context.setHandler(resource);
            context.setContextPath('/' + name);
            context.setInitParameter("gzip", "true");
            staticHandlers.add(context);
        }
        return staticHandlers;
    }

    private Handler createGuiceServletHandler() {
        ServletContextHandler servletHandler = new ServletContextHandler();
        servletHandler.addEventListener(new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                return Guice.createInjector(new JerseyGuiceModule());
            }
        });
        for (Class<? extends Filter> filter : getFilters()) {
            servletHandler.addFilter(filter, "/*", null);
        }
        servletHandler.setErrorHandler(getErrorHandler());
        servletHandler.addServlet(getDefaultServletClass(), "/");
        servletHandler.setContextPath("/");
        GzipHandler gzip = new GzipHandler();
        gzip.setHandler(servletHandler);
        return gzip;
    }
}
