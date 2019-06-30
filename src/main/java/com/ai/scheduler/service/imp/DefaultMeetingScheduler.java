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

    private final List<DayEvent> dayEventsWithFixedTalks;

    public DefaultMeetingScheduler(List<DayEvent> dayEvents) {
        super(dayEvents);
        dayEventsWithFixedTalks = new LinkedList<>();
    }

    /**
     * Schedule for unfixed talk, greedy algorithm.
     * <p>
     * Put the longest talk into smallest available slot,
     * <p>
     * if smallest slot is short than the given talk, then go to second smallest
     * <p>
     * until there is a slot is larger or equals than the given
     *
     * @param talks
     */
    protected void scheduleFlexibleTalks(List<DayEvent> dayEvents, LinkedList<Talk> talks) {
        if (talks.size() == 0) {
            // break the recursive
            return;
        }

        // clone dayEvents for scheduling
        List<DayEvent> cloneDayEvents = Utils.deepCopy(dayEvents);
        // clone a clean dayEvent for recursive call
        List<DayEvent> cleanDayEvent = Utils.deepCopy(dayEvents);
        LinkedList<Talk> notYetScheduledTalks = new LinkedList<>();
        LinkedList<Event> bookedEvents = new LinkedList<>();

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
     * Schedule for fixed time talks, greedy algorithm.
     */
    protected void scheduleFixedTimeTalks(List<DayEvent> dayEvents, LinkedList<Talk> fixedTimeTalks) {
        int bookedTalks = 0;
        while (fixedTimeTalks.size() > bookedTalks) {
            for (Talk talk : fixedTimeTalks) {
                LocalTime startTime = Utils.getFixedTalkStartTime(talk);
                for (DayEvent dayEvent : dayEvents) {
                    if (!Utils.isBooked(startTime, dayEvent)) {
                        dayEvent.add(new Event(dayEvent.getDate(), startTime, talk));
                        bookedTalks++;
                        break;
                    }
                }
            }
        }
        // keep a copy
        dayEventsWithFixedTalks.addAll(dayEvents);
    }


}
