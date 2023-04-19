package info.dmerej.huddle;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MeetingTests {
    @Test
    void meeting_knows_about_first_timers() {
        var storage = new FakeStorage();
        var aliceIdentity = new Identity("alice", "alice@domain.tld");
        var tddAnnounce = new HuddleAnnounce("2023-04-12 AM", "Learning TDD");
        var tddHuddle = storage.scheduleHuddle(tddAnnounce);
        var aliceAccount = storage.createAccount(aliceIdentity);
        var alice = storage.registerParticipant(aliceAccount, tddHuddle);
        var meeting = new Meeting(storage, tddHuddle);

        var firstParticipants = meeting.firstParticipants();
        
        assertThat(firstParticipants).isEqualTo(List.of(alice));
    }
}
