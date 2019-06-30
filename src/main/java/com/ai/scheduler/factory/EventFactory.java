package com.ai.scheduler.factory;

import com.ai.scheduler.model.Event;
import com.ai.scheduler.model.Talk;
import com.ai.scheduler.model.TalkType;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Event factory
 */
public final class EventFactory {

    private static final LocalTime LUNCH_START_TIME = LocalTime.of(12, 30);
    private static final LocalTime TEA_START_TIME = LocalTime.of(15, 00);

    private EventFactory(){}

    public static Event createLunchEvent(LocalDate eventDate) {
        Talk talk = new Talk();
        talk.setType(TalkType.LUNCH);
        return new Event(eventDate, LUNCH_START_TIME, talk);
    }

    public static Event createTeaEvent(LocalDate eventDate) {
        Talk talk = new Talk();
        talk.setType(TalkType.TEA);
        return new Event(eventDate, TEA_START_TIME, talk);
    }
}
