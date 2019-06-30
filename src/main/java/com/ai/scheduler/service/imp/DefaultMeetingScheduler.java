package com.ai.scheduler.service.imp;

import com.ai.scheduler.model.DayEvent;
import com.ai.scheduler.model.Event;
import com.ai.scheduler.model.Talk;
import com.ai.scheduler.model.Talks;
import com.ai.scheduler.service.MeetingSchedulerTemplate;
import com.ai.scheduler.util.Utils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Default meeting scheduler
 */
@Slf4j
@Getter
public class DefaultMeetingScheduler extends MeetingSchedulerTemplate {

    private final List<Talk> unScheduledTalks;

    private final List<DayEvent> dayEventsWithFixedTalks;


    public DefaultMeetingScheduler(List<DayEvent> dayEvents) {
        super(dayEvents);
        unScheduledTalks = new LinkedList<>();
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
    protected void scheduleFlexibleTalks(List<DayEvent> dayEvents, Talks talks) {
        // clone a clean dayEvents with fixed talks booked
        List<DayEvent> cloneDayEvents = (List<DayEvent>) ((LinkedList<DayEvent>) dayEvents).clone();

        LinkedList<Talk> unFixedTalks = talks.getTalks();
        //desc
        Collections.sort(unFixedTalks);
        // loop ends either talks are all booked or availableSlots are all used
        while (unFixedTalks.size() > 0) {
            LinkedList<DayEvent.AvailableSlot> availableSlots = Utils.getAvailableSlots(dayEvents);
            if (availableSlots == null || availableSlots.size() == 0) {
                break;
            }
            // asc
            Collections.sort(availableSlots);
            Talk talk = unFixedTalks.pollFirst();
            int longestAvailableSlot = Utils.getLongestAvailableSlot(cloneDayEvents);
            log.info("longestAvailableSlot is {}", longestAvailableSlot);
            if (Utils.getDuration(talk) > longestAvailableSlot) {
                log.warn("Talk {} duration is longer than longest available slot {}", talk, longestAvailableSlot);
                this.unScheduledTalks.add(talk);
                continue;
            }
            for (DayEvent.AvailableSlot availableSlot : availableSlots) {
                if (availableSlot.getDuration() >= Utils.getDuration(talk)) {
                    availableSlot.getDayEvent()
                            .add(new Event(availableSlot.getDayEvent().getDate(),
                                    availableSlot.getStartTime(), talk));
                    break;
                }
            }
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

    @Override
    public boolean hasUnScheduledTalks() {
        return getUnScheduledTalks().size() > 0;
    }

    @Override
    public List<Talk> getUnScheduledTalks() {
        return null;
    }
}
