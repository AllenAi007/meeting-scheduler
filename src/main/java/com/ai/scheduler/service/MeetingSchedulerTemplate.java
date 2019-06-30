package com.ai.scheduler.service;

import com.ai.scheduler.model.DayEvent;
import com.ai.scheduler.model.Talk;
import com.ai.scheduler.model.Talks;
import com.ai.scheduler.util.Utils;

import java.util.LinkedList;
import java.util.List;


/**
 * Template Design pattern.
 * <p>
 * You need to implement scheduleFlexibleTalks and scheduleFixedTimeTalks
 */
public abstract class MeetingSchedulerTemplate implements MeetingScheduler {

    private final List<DayEvent> dayEvents;

    public MeetingSchedulerTemplate(List<DayEvent> dayEvents) {
        this.dayEvents = dayEvents;
    }

    /**
     * Abstract schedule implementation.
     *
     * @param talks
     * @return
     */
    @Override
    public final List<DayEvent> schedule(Talks talks) {

        LinkedList<Talk> fixedTimeTalks = Utils.getFixedTimeMeeting(talks);
        scheduleFixedTimeTalks(this.dayEvents, fixedTimeTalks);

        talks.getTalks().removeAll(fixedTimeTalks);
        scheduleFlexibleTalks(this.dayEvents, talks);

        return this.dayEvents;

    }

    /**
     * Schedule unfixed talk talks for given dayEvents
     *
     * @param dayEvents
     * @param flexibleTalks
     */
    protected abstract void scheduleFlexibleTalks(List<DayEvent> dayEvents, Talks flexibleTalks);

    /**
     * Schedule unfixed talk talks for given dayEvents
     *
     * @param dayEvents
     * @param fixedTimeTalks
     */
    protected abstract void scheduleFixedTimeTalks(List<DayEvent> dayEvents, LinkedList<Talk> fixedTimeTalks);

}
