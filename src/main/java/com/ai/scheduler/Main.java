package com.ai.scheduler;


import com.ai.scheduler.factory.DayEventFactory;
import com.ai.scheduler.factory.TalksFactory;
import com.ai.scheduler.model.DayEvent;
import com.ai.scheduler.model.Talks;
import com.ai.scheduler.service.ExceptionHandler;
import com.ai.scheduler.service.MeetingScheduler;
import com.ai.scheduler.service.imp.DefaultExceptionHandler;
import com.ai.scheduler.service.imp.DefaultMeetingScheduler;
import com.ai.scheduler.util.Utils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {
        new Main(new DefaultMeetingScheduler(DayEventFactory.createDayEvents()),
                DefaultExceptionHandler.getInstance())
                .run(args);
    }

    @Getter
    private final MeetingScheduler meetingScheduler;
    @Getter
    private final ExceptionHandler exceptionHandler;

    public Main(MeetingScheduler meetingScheduler, ExceptionHandler exceptionHandler) {
        this.meetingScheduler = meetingScheduler;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Run application
     *
     * @param args
     * @return List<DayEvent> with scheduled talks
     */
    public List<DayEvent> run(String[] args) {
        try {
            return doRun(args);
        } catch (Throwable e) {
            this.exceptionHandler.handle(e);
        }
        return null;
    }

    protected List<DayEvent> doRun(String[] args) {
        if (args != null && args.length > 1) {
            log.error("Usage: run or run {absolutionTalkJsonFile}");
            return null;
        }
        Talks talks;
        if (args != null && args.length == 1) {
            talks = TalksFactory.createTalks(args[0]);
        } else {
            talks = TalksFactory.createTalks();
        }
        List<DayEvent> result = meetingScheduler.schedule(talks);
        if (result == null || result.isEmpty()) {
            return null;
        }
        // print result
        log.info("");
        log.info(Utils.getDayEventString(result));
        return result;
    }


}
