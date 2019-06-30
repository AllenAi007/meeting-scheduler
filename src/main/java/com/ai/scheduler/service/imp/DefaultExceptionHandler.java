package com.ai.scheduler.service.imp;

import com.ai.scheduler.exception.GenericException;
import com.ai.scheduler.service.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Default exception handler, just to print message as of now
 */
@Slf4j
public class DefaultExceptionHandler implements ExceptionHandler {

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
    }

    /**
     * Handle business exception which is defined according, just print the log info
     *
     * @param exception
     */
    protected void handleGenericException(GenericException exception) {
        log.error("Generic exception caused", exception);
    }
}
