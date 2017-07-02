package superunknown;

import static java.lang.String.format;

import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;

public class Log {

    private static final String CONTEXT = "ctx";
    private static String LOG_PATTERN =
            "%date{yyyy-MM-dd'T'HH:mm:ss.SSS'Z', UTC} %level %logger{15}:%mdc{ctx} %msg %ex{3} %n";

    static void configLog(String logLevel) {
        LoggerContext logCtx = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger log = logCtx.getLogger(Logger.ROOT_LOGGER_NAME);
        log.detachAndStopAllAppenders();
        log.setAdditive(false);
        Level level = Level.valueOf(logLevel);
        log.setLevel(level != null? level: Level.ALL);

        PatternLayout layout = new PatternLayout();
        layout.setPattern(LOG_PATTERN);
        layout.setContext(logCtx);
        layout.start();

        ConsoleAppender<ILoggingEvent> logConsoleAppender = new ConsoleAppender<>();
        logConsoleAppender.setContext(logCtx);
        logConsoleAppender.setName("console");
        logConsoleAppender.setLayout(layout);
        logConsoleAppender.start();

        log.addAppender(logConsoleAppender);

    }

    /**
     * Set current log context to a random UUID
     * 
     * @see MDC
     */
    public static void context() {
        context(UUID.randomUUID().toString());
    }

    /**
     * Set current log context to given context
     * 
     * @see MDC
     */
    public static void context(String context) {
        MDC.put(CONTEXT, format("%s:", context));
    }

    public static void debug(String msg) {
        LoggerFactory.getLogger(getCallerClassName()).debug(msg);
    }

    public void debug(String format, Object... arguments) {
        LoggerFactory.getLogger(getCallerClassName()).debug(format, arguments);
    }

    public static void debug(String msg, Throwable t) {
        LoggerFactory.getLogger(getCallerClassName()).debug(msg, t);
    }

    public void info(String msg) {
        LoggerFactory.getLogger(getCallerClassName()).info(msg);
    }

    public static void info(String format, Object... arguments) {
        LoggerFactory.getLogger(getCallerClassName()).info(format, arguments);
    }

    public static void info(String msg, Throwable t) {
        LoggerFactory.getLogger(getCallerClassName()).info(msg, t);
    }

    public static void warn(String msg) {
        LoggerFactory.getLogger(getCallerClassName()).warn(msg);
    }

    public static void warn(String format, Object... arguments) {
        LoggerFactory.getLogger(getCallerClassName()).warn(format, arguments);
    }

    public static void warn(String msg, Throwable t) {
        LoggerFactory.getLogger(getCallerClassName()).debug(msg, t);
    }

    public static void error(String msg) {
        LoggerFactory.getLogger(getCallerClassName()).error(msg);
    }

    public static void error(String format, Object... arguments) {
        LoggerFactory.getLogger(getCallerClassName()).error(format, arguments);
    }

    public static void error(String msg, Throwable t) {
        LoggerFactory.getLogger(getCallerClassName()).error(msg, t);
    }

    private static String getCallerClassName() {
        return Thread.currentThread().getStackTrace()[2].getClassName();
    }
}
