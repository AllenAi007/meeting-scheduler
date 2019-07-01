package com.ai.scheduler.service.imp;

import com.ai.scheduler.exception.GenericException;
import com.ai.scheduler.service.ExceptionHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Default exception handler.
 * <p>
 * Singleton
 */
@Getter
@Slf4j
public class DefaultExceptionHandler implements ExceptionHandler {

    private List<Throwable> unknownExceptions = new CopyOnWriteArrayList<>();
    private List<GenericException> genericExceptions = new CopyOnWriteArrayList<>();

    private DefaultExceptionHandler() {
    }

    public static DefaultExceptionHandler getInstance() {
        return DefaultExceptionHandlerHolder.INSTANCE;
    }

    @Override
    public final void handle(Throwable exception) {
        if (exception instanceof GenericException) {
            handleGenericException((GenericException) exception);
        } else {
            handleUnknownException(exception);
        }
    }

    /**
     * Handle other exceptions, just print log
     *
     * @param exception
     */
    protected void handleUnknownException(Throwable exception) {
        log.error("Unknown exception caused", exception);
        unknownExceptions.add(exception);
    }

    /**
     * Handle business exception which is defined according, just print the log info
     *
     * @param exception
     */
    protected void handleGenericException(GenericException exception) {
        log.error("Generic exception caused", exception);
        genericExceptions.add(exception);
    }

    private static class DefaultExceptionHandlerHolder {
        private static DefaultExceptionHandler INSTANCE = new DefaultExceptionHandler();
    }

    /**
     * Clean all exception
     */
    public void clear() {
        this.genericExceptions.clear();
        this.unknownExceptions.clear();
    }
}
