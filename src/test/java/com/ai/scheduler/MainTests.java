package com.ai.scheduler;

import com.ai.scheduler.exception.InitializationException;
import com.ai.scheduler.exception.UnknownTalkTypeException;
import com.ai.scheduler.factory.DayEventFactory;
import com.ai.scheduler.factory.TalksFactory;
import com.ai.scheduler.model.DayEvent;
import com.ai.scheduler.service.imp.DefaultExceptionHandler;
import com.ai.scheduler.service.imp.DefaultMeetingScheduler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MainTests {

    private Main classUnderTest;

    @Before
    public void before() {
        classUnderTest = new Main(
                new DefaultMeetingScheduler(DayEventFactory.createDayEvents()),
                new DefaultExceptionHandler());
    }

    @Test
    public void given_lessTalks_return_event_scheduled_day_events() {
        List<DayEvent> result = classUnderTest.run(null);
        int totalEventBooked = result.stream()
                .map(DayEvent::getEvents)
                .reduce((a, b) -> {
                    a.addAll(b);
                    return a;
                })
                .get()
                .size() - 2 * result.size(); // minutes lUNCH and TEA
        int totalTalks = TalksFactory.createTalks().getTalks().size();
        Assert.assertEquals(totalEventBooked, totalTalks);
    }

    @Test
    public void given_more_talks_return_event_scheduled_day_events() {
        String moreTalksFile = "more_talks.json";
        String absolutePath = Thread.currentThread().getContextClassLoader().getResource(moreTalksFile).getFile();
        List<DayEvent> result = classUnderTest.run(new String[]{absolutePath});
        int totalEventBooked = result.stream()
                .map(DayEvent::getEvents)
                .reduce((a, b) -> {
                    a.addAll(b);
                    return a;
                })
                .get()
                .size() - 2 * result.size(); // minutes lUNCH and TEA
        int totalTalks = TalksFactory.createTalks(absolutePath).getTalks().size();
        Assert.assertEquals(totalEventBooked, totalTalks);
    }

    @Test(expected = InitializationException.class)
    public void given_invalid_json_will_throw_exception() {
        String moreTalksFile = "talks_result.txt";
        String absolutePath = Thread.currentThread().getContextClassLoader().getResource(moreTalksFile).getFile();
        classUnderTest.run(new String[]{absolutePath});
    }

    @Test(expected = InitializationException.class)
    public void given_json_with_unknown_talk_type_will_throw_exception() {
        String moreTalksFile = "unknown_type_talks.json";
        String absolutePath = Thread.currentThread().getContextClassLoader().getResource(moreTalksFile).getFile();
        classUnderTest.run(new String[]{absolutePath});
    }

}
