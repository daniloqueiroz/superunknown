package server.internal;

import static java.lang.String.valueOf;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Simple filter to set context (MDC) for logs and log requests.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class MDCLogFilter implements Filter {

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