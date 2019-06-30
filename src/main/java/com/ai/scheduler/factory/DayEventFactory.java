package com.ai.scheduler.factory;

import com.ai.scheduler.model.DayEvent;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Create DayEvent
 */
public final class DayEventFactory {

    private static final LocalTime DAYEVENT_START = LocalTime.of(9, 0);
    private static final LocalTime DAYEVENT_END = LocalTime.of(19, 30);

    private DayEventFactory(){}

    /**
     * Create day event with fixed event lunch and tea
     *
     * @param year
     * @param month
     * @param day
     * @return DayEvent
     */
    public static DayEvent createDayEvent(int year, int month, int day) {
        LocalDate eventDate = LocalDate.of(year, month, day);
        DayEvent dayEvent = new DayEvent(eventDate, DAYEVENT_START, DAYEVENT_END);
        dayEvent.add(EventFactory.createLunchEvent(eventDate));
        dayEvent.add(EventFactory.createTeaEvent(eventDate));
        return dayEvent;
    }
}
