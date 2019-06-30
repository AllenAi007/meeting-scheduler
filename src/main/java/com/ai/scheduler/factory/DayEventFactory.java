package com.ai.scheduler.factory;

import com.ai.scheduler.model.DayEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Create DayEvent
 */
public final class DayEventFactory {

    private static final LocalTime DAYEVENT_START = LocalTime.of(9, 0);
    private static final LocalTime DAYEVENT_END = LocalTime.of(19, 30);

    private DayEventFactory() {
    }

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

    /**
     * Create default day events
     *
     * @return List<DayEvent>
     */
    public static List<DayEvent> createDayEvents() {
        List<DayEvent> result = new LinkedList<>();
        result.add(createDayEvent(2018, 06, 29));
        result.add(createDayEvent(2018, 06, 30));
        return result;
    }
}
