package com.ai.scheduler.service;

import com.ai.scheduler.model.DayEvent;
import com.ai.scheduler.model.Talks;

import java.util.List;

public interface MeetingScheduler {

    /**
     * Schedule meeting by given talks
     * @param talks
     * @return List<DayEvent> result
     */
    List<DayEvent> schedule(Talks talks);

}
