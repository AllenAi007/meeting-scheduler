package com.ai.scheduler;


import com.ai.scheduler.factory.DayEventFactory;
import com.ai.scheduler.factory.TalksFactory;
import com.ai.scheduler.model.DayEvent;
import com.ai.scheduler.model.Talks;
import com.ai.scheduler.service.SimpleMeetingScheduler;
import com.ai.scheduler.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {
        if (args != null && args.length > 1) {
            log.error("Usage: run or run {absolutionTalkJsonFile}");
            System.exit(1);
        }
        Talks talks;
        if (args != null && args.length == 1) {
            talks = TalksFactory.createTalks(args[0]);
        } else {
            talks = TalksFactory.createTalks();
        }
        List<DayEvent> dayEvents = Arrays.asList(DayEventFactory.createDayEvent(2018, 06, 29),
                DayEventFactory.createDayEvent(2018, 06, 30));
        SimpleMeetingScheduler meetingScheduler = new SimpleMeetingScheduler(dayEvents);
        List<DayEvent> result = meetingScheduler.schedule(talks);
        log.info(Utils.getDayEventString(result));
    }


}
