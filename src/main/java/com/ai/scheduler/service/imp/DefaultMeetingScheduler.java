package com.ai.scheduler.service.imp;

import com.ai.scheduler.model.DayEvent;
import com.ai.scheduler.model.Event;
import com.ai.scheduler.model.Talk;
import com.ai.scheduler.model.TalkType;
import com.ai.scheduler.service.MeetingSchedulerTemplate;
import com.ai.scheduler.util.Utils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default meeting scheduler
 */
@Slf4j
@Getter
public class DefaultMeetingScheduler extends MeetingSchedulerTemplate {

    public DefaultMeetingScheduler(List<DayEvent> dayEvents) {
        super(dayEvents);
    }

    /**
     * 1. Schedule for flexible talk, greedy algorithm.
     * Put the longest talk into smallest available slot,
     * if smallest slot is short than the given talk, then go to second smallest
     * until there is a slot is larger or equals than the given
     * <p>
     * 2. After one round fill, if there are still talks which are not scheduled, call #1 with a clone dayEvents(with only fixed talks)
     * and not yet scheduled talks. And keep doing util yet scheduled talks is empty
     *
     * @param dayEvents
     * @param talks
     */
    protected void scheduleFlexibleTalks(List<DayEvent> dayEvents, LinkedList<Talk> talks) {
        if (talks.isEmpty()) {
            // break the recursive
            return;
        }

        // clone dayEvents for scheduling
        List<DayEvent> cloneDayEvents = Utils.deepCopy(dayEvents);
        // clone a clean dayEvent for recursive call
        List<DayEvent> cleanDayEvent = Utils.deepCopy(dayEvents);
        LinkedList<Talk> notYetScheduledTalks = new LinkedList<>();

        // desc
        Collections.sort(talks);
        // loop ends either talks are all booked or availableSlots are all used
        while (talks.size() > 0) {
            LinkedList<DayEvent.AvailableSlot> availableSlots = Utils.getAvailableSlots(cloneDayEvents);
            if (availableSlots == null || availableSlots.size() == 0) {
                break;
            }
            // asc
            Collections.sort(availableSlots);
            Talk talk = talks.pollFirst();
            boolean findSlot = false;
            for (DayEvent.AvailableSlot availableSlot : availableSlots) {
                if (availableSlot.getDuration() >= Utils.getDuration(talk)) {
                    availableSlot.getDayEvent()
                            .add(new Event(availableSlot.getDayEvent().getDate(),
                                    availableSlot.getStartTime(), talk));
                    findSlot = true;
                    break;
                }
            }
            // not find a slot yet
            if (!findSlot) {
                notYetScheduledTalks.add(talk);
            }
        }

        // copy clone into real
        copyCloneToReal(cloneDayEvents);
        scheduleFlexibleTalks(cleanDayEvent, notYetScheduledTalks);
    }

    private void copyCloneToReal(List<DayEvent> cloneDayEvents) {
        for (int i = 0; i < cloneDayEvents.size(); i++) {
            DayEvent dayEvent = cloneDayEvents.get(i);
            getDayEvents().get(i).getEvents().addAll(
                    dayEvent.getEvents().stream()
                            .filter(e -> !Utils.isFixedTalk(e.getTalk()))
                            .collect(Collectors.toList())
            );
        }
    }


    /**
     * Schedule for fixed time talks, round robin algorithm
     */
    protected void scheduleFixedTimeTalks(List<DayEvent> dayEvents, LinkedList<Talk> fixedTimeTalks) {

        LinkedList<Talk> notYetBookedTalks = new LinkedList<>();
        // first round, find available slot and booked directly
        for (Talk talk : fixedTimeTalks) {
            LocalTime startTime = Utils.getFixedTalkStartTime(talk);
            boolean booked = false;
            for (DayEvent dayEvent : dayEvents) {
                if (!Utils.isBooked(startTime, dayEvent)) {
                    dayEvent.add(new Event(dayEvent.getDate(), startTime, talk));
                    booked = true;
                    break;
                }
            }
            if (!booked) {
                notYetBookedTalks.add(talk);
            }
        }

        // since not available slot left, round robin
        if (!notYetBookedTalks.isEmpty()) {
            for (int i = 0; i < notYetBookedTalks.size(); i++) {
                int robin = i % dayEvents.size();
                DayEvent toBeScheduleDayEvent = dayEvents.get(robin);
                Talk talk = notYetBookedTalks.get(i);
                LocalTime startTime = Utils.getFixedTalkStartTime(talk);
                toBeScheduleDayEvent.add(new Event(toBeScheduleDayEvent.getDate(), startTime, talk));
            }
        }
    }


}
