package com.ai.scheduler.service.imp;

import com.ai.scheduler.exception.InitializationException;
import com.ai.scheduler.exception.MeetingScheduleException;
import com.ai.scheduler.service.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Default exception handler, just to print message as of now
 */
@Slf4j
public class DefaultExceptionHandler implements ExceptionHandler {

    @Override
    public final void handle(Throwable exception) {
        if (exception instanceof MeetingScheduleException) {
            handleBusinessException((MeetingScheduleException) exception);
        } else {
            handleTechnicalException(exception);
        }
    }

    /**
     * Handle other exceptions, just print log
     *
     * @param exception
     */
    protected void handleTechnicalException(Throwable exception) {
        log.error("Technical exception caused", exception);
    }

    /**
     * Handle business exception which is defined according, just print the log info
     *
     * @param exception
     */
    protected void handleBusinessException(MeetingScheduleException exception) {
        log.error("Business exception caused", exception);
    }
}
