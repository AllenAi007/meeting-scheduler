package com.ai.scheduler.util;

import com.ai.scheduler.exception.CloneFailedException;
import com.ai.scheduler.exception.UnknownTalkTypeException;
import com.ai.scheduler.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Utilities class
 */
@Slf4j
public final class Utils {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());


    /**
     * Avoid initialization
     */
    private Utils() {
    }

    /**
     * Get AvailableSlot from given dayEvent
     *
     * @param dayEvent
     * @return
     */
    public static LinkedList<DayEvent.AvailableSlot> getAvailableSlots(DayEvent dayEvent) {
        LinkedList<DayEvent.AvailableSlot> result = new LinkedList<>();
        Event previous = null;
        for (Event current : dayEvent.getEvents()) {
            // first event
            if (previous == null) {
                previous = current;
                continue;
            }
            if (previous.getEndTime().isBefore(current.getStartTime())) {
                result.add(new DayEvent.AvailableSlot(dayEvent,
                        previous.getEndTime(), current.getStartTime()));
            }
            previous = current;
        }
        return result;
    }

    /**
     * Get all day events
     *
     * @param dayEvents
     * @return
     */
    public static LinkedList<DayEvent.AvailableSlot> getAvailableSlots(List<DayEvent> dayEvents) {
        dayEvents.forEach(dayEvent -> Collections.sort(dayEvent.getEvents()));
        return dayEvents.stream()
                .map(Utils::getAvailableSlots)
                .reduce((a, b) -> {
                    a.addAll(b);
                    return a;
                }).get();
    }

    /**
     * Get duration for a talk
     *
     * @return minutes of talk duration
     */
    public static int getDuration(Talk talk) {
        switch (talk.getType()) {
            case WORKSHOP:
            case LUNCH:
            case PANEL_DISCUSSION:
                return 60;
            case TEA:
                return 15;
            case REGULAR_TALK:
            case KEYNOTE:
            case CLOSING:
                return 30;
            case LIGHTNING:
                return 10;
            default:
                throw new UnknownTalkTypeException("Unknown talk type ->" + talk.getType());
        }
    }

    /**
     * Check whether for the proposalStartTime whether it is booked or not
     *
     * @param proposalStartTime proposal startTime
     * @param dayEvent          given DayEvent
     * @return true if it is booked
     */
    public static boolean isBooked(LocalTime proposalStartTime, DayEvent dayEvent) {
        return dayEvent.getEvents()
                .stream()
                .map(d -> d.getStartTime())
                .anyMatch(localTime -> localTime.equals(proposalStartTime));
    }

    public static LocalTime getFixedTalkStartTime(Talk talk) {
        switch (talk.getType()) {
            case CLOSING:
                return LocalTime.of(17, 0);
            case KEYNOTE:
                return LocalTime.of(9, 0);
            default:
                throw new UnknownTalkTypeException("Unknown talk type ->" + talk.getType());
        }
    }

    public static LinkedList<Talk> getFixedTimeMeeting(Talks talks) {
        LinkedList<Talk> result = new LinkedList<>();
        for (Talk talk : talks.getTalks()) {
            if (TalkType.KEYNOTE.equals(talk.getType())
                    || TalkType.CLOSING.equals(talk.getType())) {
                result.add(talk);
            }
        }
        return result;
    }

    /**
     * Get day event string from dayEventList
     *
     * @param dayEvent given dayEvent
     */
    public static String getDayEventString(DayEvent dayEvent) {
        Collections.sort(dayEvent.getEvents());
        StringBuilder stringBuilder = new StringBuilder();
        dayEvent.getEvents().forEach(event -> {
            stringBuilder.append(event.getStartTime());
            stringBuilder.append("\t");
            if (event.getTalk().getDescription() != null) {
                stringBuilder.append(event.getTalk().getDescription());
                stringBuilder.append("\t");
            }
            stringBuilder.append(event.getTalk().getType());
            stringBuilder.append("\n");
        });
        return stringBuilder.toString();
    }

    /**
     * Get day event string from dayEventList
     *
     * @param dayEventList
     */
    public static String getDayEventString(List<DayEvent> dayEventList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < dayEventList.size(); i++) {
            stringBuilder.append("Day " + (i + 1) + " Track " + (i + 1) + ":");
            stringBuilder.append("\n");
            stringBuilder.append(getDayEventString(dayEventList.get(i)));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Deep copy
     *
     * @param dayEvents given dayEvents
     * @return
     */
    public static List<DayEvent> deepCopy(List<DayEvent> dayEvents) {
        List<DayEvent> result = new LinkedList<>();
        try {
            for (DayEvent dayEvent : dayEvents) {
                result.add(OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsBytes(dayEvent), DayEvent.class));
            }
        } catch (IOException e) {
            throw new CloneFailedException("Failed while deep copy dayEvents " + dayEvents, e);
        }
        return result;
    }

    public static boolean isFixedTalk(Talk talk) {
        return TalkType.KEYNOTE.equals(talk.getType())
                || TalkType.CLOSING.equals(talk.getType())
                || TalkType.LUNCH.equals(talk.getType())
                || TalkType.TEA.equals(talk.getType());
    }

}
