package com.ai.scheduler;

import com.ai.scheduler.exception.InitializationException;
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

    private DefaultExceptionHandler exceptionHandler;

    @Before
    public void before() {
        exceptionHandler = DefaultExceptionHandler.getInstance();
        classUnderTest = new Main(
                new DefaultMeetingScheduler(DayEventFactory.createDayEvents()),
                exceptionHandler);
        exceptionHandler.clear();
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
    public void given_more_talks_return_day_events_with_all_talks_scheduled() {
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

    @Test
    public void given_more_talks_with_more_fixed_event_return_day_events_with_all_talks_scheduled() {
        String moreTalksFile = "more_fixed_talks.json";
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

    @Test
    public void given_invalid_json_will_throw_exception_and_can_be_handled() {
        String moreTalksFile = "talks_result.txt";
        String absolutePath = Thread.currentThread().getContextClassLoader().getResource(moreTalksFile).getFile();
        classUnderTest.run(new String[]{absolutePath});
        Assert.assertEquals(1, exceptionHandler.getGenericExceptions().size());
        Assert.assertTrue(exceptionHandler.getGenericExceptions().get(0) instanceof InitializationException);

    }

    @Test
    public void given_json_with_unknown_talk_type_will_throw_exception_and_can_be_handled() {
        String moreTalksFile = "unknown_type_talks.json";
        String absolutePath = Thread.currentThread().getContextClassLoader().getResource(moreTalksFile).getFile();
        classUnderTest.run(new String[]{absolutePath});
        Assert.assertEquals(1, exceptionHandler.getGenericExceptions().size());
        Assert.assertTrue(exceptionHandler.getGenericExceptions().get(0) instanceof InitializationException);
    }

    @Test
    public void given_empty_talk_will_return_null() {
        String moreTalksFile = "empty_talks.json";
        String absolutePath = Thread.currentThread().getContextClassLoader().getResource(moreTalksFile).getFile();
        List<DayEvent> dayEvents = classUnderTest.run(new String[]{absolutePath});
        Assert.assertNull(dayEvents);
    }

    @Test
    public void given_one_fixed_talk_will_return_day_event_list_with_one_fixed_talk_scheduled() {
        String oneFixedTalk = "one_fixed_talk.json";
        String absolutePath = Thread.currentThread().getContextClassLoader().getResource(oneFixedTalk).getFile();
        List<DayEvent> result = classUnderTest.run(new String[]{absolutePath});
        int totalEventBooked = result.stream()
                .map(DayEvent::getEvents)
                .reduce((a, b) -> {
                    a.addAll(b);
                    return a;
                })
                .get()
                .size() - 2 * result.size(); // minutes lUNCH and TEA
        Assert.assertEquals(totalEventBooked, 1);
    }

    @Test
    public void given_one_flexible_talk_will_return_day_event_list_with_one_flexible_talk_scheduled() {
        String oneFlexibleTalk = "one_flexible_talk.json";
        String absolutePath = Thread.currentThread().getContextClassLoader().getResource(oneFlexibleTalk).getFile();
        List<DayEvent> result = classUnderTest.run(new String[]{absolutePath});
        int totalEventBooked = result.stream()
                .map(DayEvent::getEvents)
                .reduce((a, b) -> {
                    a.addAll(b);
                    return a;
                })
                .get()
                .size() - 2 * result.size(); // minutes lUNCH and TEA
        Assert.assertEquals(totalEventBooked, 1);
    }

    @Test
    public void given_one_fixed_and_one_flexible_talk_will_return_day_event_list_with_one_fixed_and_one_flexible_talk_scheduled() {
        String oneFixedAndOneFlexibleTalk = "one_fixed_and_one_flexible_talks.json";
        String absolutePath = Thread.currentThread().getContextClassLoader().getResource(oneFixedAndOneFlexibleTalk).getFile();
        List<DayEvent> result = classUnderTest.run(new String[]{absolutePath});
        int totalEventBooked = result.stream()
                .map(DayEvent::getEvents)
                .reduce((a, b) -> {
                    a.addAll(b);
                    return a;
                })
                .get()
                .size() - 2 * result.size(); // minutes lUNCH and TEA
        Assert.assertEquals(totalEventBooked, 2);
    }

}
