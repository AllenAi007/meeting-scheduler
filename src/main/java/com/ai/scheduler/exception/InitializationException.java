package com.ai.scheduler.exception;

public class InitializationException extends MeetingScheduleException {

    public InitializationException(String message) {
        super(message);
    }

    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }

}
