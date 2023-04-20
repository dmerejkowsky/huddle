package info.dmerej.huddle;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CalendarTests {
    @Test
    void retrieving_past_huddles() {
        var storage = new FakeStorage();
        var learningTdd = new HuddleAnnounce("2021-12-10 AM", "Learning TDD");
        var learningKotlin = new HuddleAnnounce("2021-13-10 AM", "Learning Kotlin");
        var futureHuddle = new HuddleAnnounce("2038-01-01 PM", "In the future");
        var calendar = new Calendar(storage);
        calendar.scheduleHuddle(learningTdd);
        calendar.scheduleHuddle(learningKotlin);
        calendar.scheduleHuddle(futureHuddle);

        var pastHuddles = calendar.pastHuddles();

        assertThat(pastHuddles.stream().map(h -> h.title())).containsExactly("Learning TDD", "Learning Kotlin");
    }
}
