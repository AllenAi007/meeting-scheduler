package com.ai.scheduler.service;

import com.ai.scheduler.model.DayEvent;
import com.ai.scheduler.model.Event;
import com.ai.scheduler.model.Talk;
import com.ai.scheduler.model.Talks;
import com.ai.scheduler.util.Utils;
import lombok.Getter;

import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple meeting scheduler
 */
@Getter
public class SimpleMeetingScheduler extends MeetingSchedulerTemplate {


    public SimpleMeetingScheduler(List<DayEvent> dayEvents) {
        super(dayEvents);
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
        LinkedList<Talk> unFixedTalks = talks.getTalks();
        Collections.sort(unFixedTalks);
        // loop ends either talks are all booked or availableSlots are all used
        while (unFixedTalks.size() > 0) {
            LinkedList<DayEvent.AvailableSlot> availableSlots = Utils.getAvailableSlots(dayEvents);
            if (availableSlots == null || availableSlots.size() == 0) {
                break;
            }
            Collections.sort(availableSlots);
            Talk talk = unFixedTalks.pollFirst();
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

    }
}
