package com.ai.scheduler.exception;

public class MeetingScheduleException extends RuntimeException {

    public MeetingScheduleException() {
    }

    public MeetingScheduleException(String message) {
        super(message);
    }

    public MeetingScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}
