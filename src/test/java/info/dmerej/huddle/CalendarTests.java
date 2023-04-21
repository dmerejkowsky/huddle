package info.dmerej.huddle;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class CalendarTests {
    @Test
    void retrieving_all_huddles() {
        var storage = new FakeStorage();
        var learningTdd = new HuddleAnnounce("2021-12-10 AM", "Learning TDD");
        var learningKotlin = new HuddleAnnounce("2021-13-10 AM", "Learning Kotlin");
        var calendar = new Calendar(storage);
        calendar.scheduleHuddle(learningTdd);
        calendar.scheduleHuddle(learningKotlin);

        var pastHuddles = calendar.allHuddles();

        assertThat(pastHuddles.stream().map(h -> h.title())).containsExactly("Learning TDD", "Learning Kotlin");
    }

    @Test
    void list_first_timers() {
        var storage = new FakeStorage();
        var calendar = new Calendar(storage);

        var oldAnnounce = new HuddleAnnounce("2021-01-01 AM", "First ever huddle");
        var bobAccount = storage.createAccount(new Identity("bob", "bob@domain.tld"));
        var oldHuddle = storage.scheduleHuddle(oldAnnounce);
        storage.registerParticipant(bobAccount, oldHuddle);

        var tddAnnounce = new HuddleAnnounce("2023-04-12 AM", "Learning TDD");
        var tddHuddle = storage.scheduleHuddle(tddAnnounce);
        var aliceAccount = storage.createAccount(new Identity("alice", "alice@domain.tld"));
        var alice = storage.registerParticipant(aliceAccount, tddHuddle);
        var bob = storage.registerParticipant(bobAccount, tddHuddle);

        var firstTimeParticipants = calendar.firstTimeParticipants(tddHuddle);

        assertThat(firstTimeParticipants).isEqualTo(List.of(alice));
    }
}
