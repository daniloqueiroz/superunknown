package server;

import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;

import server.internal.MDCLogFilter;

import com.google.inject.servlet.GuiceFilter;

import datastore.jdo.PersistenceManagerTransactionFilter;

/**
 * This class provides a set of static methods to customize the Jersey
 * {@link Handler} creation.
 * 
 * Change this methods to add new filters, change error handler and change the
 * default servlet class.
 * 
 * @author Danilo Queiroz
 */
public class JerseyHandlerHelpers {

    private static ErrorHandler errorHandler = new ErrorPageErrorHandler();
    static {
        errorHandler.setShowStacks(true);
    }

    public static Class<DefaultServlet> getDefaultServletClass() {
        return DefaultServlet.class;
    }

    public static <T> List<Class<? extends Filter>> getFilters() {
        return Arrays.asList(MDCLogFilter.class, PersistenceManagerTransactionFilter.class,
                GuiceFilter.class);
    }

    public static ErrorHandler getErrorHandler() {
        return errorHandler;
    }

}
