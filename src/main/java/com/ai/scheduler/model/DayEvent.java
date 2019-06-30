package com.ai.scheduler.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

@Data
@Getter
@NoArgsConstructor
public class DayEvent {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private LinkedList<Event> events = new LinkedList<>();

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
    @NoArgsConstructor
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
