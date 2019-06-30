package com.ai.scheduler.service;

import com.ai.scheduler.model.DayEvent;
import com.ai.scheduler.model.Talk;
import com.ai.scheduler.model.Talks;

import java.util.List;

public interface MeetingScheduler {

    /**
     * Schedule meeting by given talks.
     *
     * @param talks given talks
     * @return List<DayEvent> result
     */
    List<DayEvent> schedule(Talks talks);

    /**
     * In case talk is too long that can't put into slots
     *
     * @return true means there are talks which are not able to be scheduled
     */
    boolean hasUnScheduledTalks();

    /**
     * Get unscheduled talks if there is any.
     *
     * @return List<Talk>
     */
    List<Talk> getUnScheduledTalks();
}
