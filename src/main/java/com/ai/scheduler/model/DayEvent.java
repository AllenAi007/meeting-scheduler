package com.ai.scheduler.model;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.TreeSet;

@Getter
public class DayEvent {
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final TreeSet<Event> events = new TreeSet();

    public DayEvent(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void add(Event event) {
        this.events.add(event);
    }


    @Data
    @Getter
    public static class AvailableSlot implements Comparable<AvailableSlot> {
        private DayEvent dayEvent;
        private LocalTime startTime;
        private LocalTime endTime;
        private int duration;

        public AvailableSlot(DayEvent dayEvent, LocalTime startTime, LocalTime endTime) {
            this.dayEvent = dayEvent;
            this.startTime = startTime;
            this.endTime = endTime;
            this.duration = (int) startTime.until(endTime, ChronoUnit.MINUTES);
        }

        @Override
        public int compareTo(AvailableSlot o) {
            return duration - o.duration;
        }
    }
}