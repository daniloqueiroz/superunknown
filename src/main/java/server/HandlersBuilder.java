package server;

import static java.io.File.separatorChar;
import static java.lang.String.valueOf;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
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
        servletHandler.addEventListener(new ServerContextListener());
        servletHandler.addFilter(MDCLogFilter.class, "/*", null);
        servletHandler.addFilter(GuiceFilter.class, "/*", null);
        servletHandler.addServlet(DefaultServlet.class, "/");
        servletHandler.setContextPath("/");
        return servletHandler;
    }

    /**
     * Configure Guice to use Jersey
     */
    static class ServerContextListener extends GuiceServletContextListener {
        @Override
        protected Injector getInjector() {
            return Guice.createInjector(new JerseyGuiceModule());
        }
    }

    public static class MDCLogFilter implements Filter {

        private static final String CONTEXT_KEY = "context";
        private static final AtomicLong counter = new AtomicLong();
        private static final Logger logger = LoggerFactory.getLogger(MDCLogFilter.class);

        /*
         * (non-Javadoc)
         * 
         * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
         * javax.servlet.ServletResponse, javax.servlet.FilterChain)
         */
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            try {
                MDC.put(CONTEXT_KEY, valueOf(counter.getAndIncrement()));
                logRequest(request);
                chain.doFilter(request, response);
                throw new IOException("fuck!");
            } finally {
                MDC.remove(CONTEXT_KEY);
            }
        }

        /**
         * @param request
         */
        private void logRequest(ServletRequest request) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            logger.info("Request received - URI: {} - Search String: {} - User-Agent: {}",
                    httpRequest.getRequestURI(), httpRequest.getQueryString(),
                    httpRequest.getHeader("User-Agent"));
        }

        @Override
        public void destroy() {
        }

        @Override
        public void init(FilterConfig arg0) throws ServletException {
        }
    }
}
