package com.perfect.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.perfect.util.ResourceUtil;


public class LoggerManager {

    /**
     * 要加载的日志文件名
     */
    private static final String NAME = "log4j2.xml";

    /**
     * 调试日志
     */
    public static final Logger DEBUG_LOG = LogManager.getLogger("DebugLog");

    /**
     * 异常日志
     */
    private static final Logger EXCEPTION = LogManager.getLogger("Exception");

    /**
     * 当前工程日志
     */
    public static final Logger PROCESS = LogManager.getLogger(System.getProperty("loggerFile"));


    static {
        ConfigurationSource source;
        try {
            URL url= ResourceUtil.getUrl(NAME);
            String path = url.getPath();

            File file = new File(path);
            InputStream stream = new FileInputStream(file);
            source = new ConfigurationSource(stream, url);
            Configurator.initialize(null, source);

//            System.setProperty("log4j.configurationFile", path);
//            LoggerContext context = (LoggerContext)LogManager.getContext(false);
//            context.reconfigure();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 错误日志
     *
     * @param message
     */
    public static void errorLogger(String message) {
        EXCEPTION.error(message);
        EXCEPTION.error(ExceptionUtils.getStackTrace(new Throwable()));
    }

    /**
     * 错误日志
     *
     * @param message
     * @param throwable
     */
    public static void errorLogger(String message, Throwable throwable) {
        EXCEPTION.error(message, throwable);
    }

    /**
     * 错误日志
     *
     * @param format
     * @param arguments
     */
    public static void errorLogger(String format, Object... arguments) {
        EXCEPTION.error(format, arguments);
        EXCEPTION.error(ExceptionUtils.getStackTrace(new Throwable()));
    }

    /**
     * 调试日志
     *
     * @param format
     * @param arguments
     */
    public static void debugLogger(String format, Object... arguments) {
        DEBUG_LOG.info(format, arguments);
    }

    /**
     * 调试日志
     */
    public static void debugStack() {
        debugStack(null);
    }

    /**
     * 调试堆栈信息日志
     *
     * @param message
     */
    public static void debugStack(String message) {
        String traceMessage = stackTrace(message);
        DEBUG_LOG.info(traceMessage);
    }

    /**
     * 调试堆栈信息
     *
     * @param message
     */
    public static String stackTrace(String message) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        if (ArrayUtils.isEmpty(stack)) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append( "Stack trace: " + message + "\n" );
        for ( int i = 0; i < stack.length; i ++ ) {
            if ( i == 0 || i == 1 ) {
                // 这个函数本身，不打印出来
                continue;
            }
            StackTraceElement trace = stack[i];
            builder.append(trace.toString() + "\n");
        }
        return builder.toString();
    }

}
