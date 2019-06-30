package com.ai.scheduler.model;

import com.ai.scheduler.util.Utils;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class Event implements Comparable<Event> {

    private final LocalDate eventDate;
    private final LocalTime startTime;
    private final Talk talk;
    // in minutes
    private final int duration;
    private final LocalTime endTime;

    public Event(LocalDate eventDate, LocalTime startTime, Talk talk) {
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.talk = talk;
        this.duration = Utils.getDuration(talk);
        this.endTime = this.startTime.plusMinutes(duration);
    }

    @Override
    public int compareTo(Event event) {
        return this.startTime.compareTo(event.startTime);
    }
}
