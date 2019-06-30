package com.ai.scheduler.model;

import com.ai.scheduler.util.Utils;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Getter
public class Event implements Comparable<Event>, Cloneable {

    private LocalDate eventDate;
    private LocalTime startTime;
    private Talk talk;
    // in minutes
    private int duration;
    private LocalTime endTime;

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

    @Override
    public Object clone() {
        try {
            return (Event) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Event(eventDate, startTime, talk);
        }
    }
}
