package info.dmerej.huddle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractStorageTest {
    protected Storage storage;

    abstract Storage getStorage();

    @BeforeEach
    public void clean() {
        storage = getStorage();
        storage.reset();
    }

    @Test
    void get_all_accounts() {
        var allAccounts = storage.getAllAccounts();
        assertThat(allAccounts).isEmpty();
    }

    @Test
    void insert_account() {
        var account = new Identity("bob", "bob@domain.tld");
        storage.createAccount(account);

        var found = storage.getAccountByUserName("bob");
        assertThat(found.username()).isEqualTo("bob");
        assertThat(found.email()).isEqualTo("bob@domain.tld");
    }

    @Test
    void insert_several_accounts_then_list_tem() {
        storage.createAccount(new Identity("alice", "alice@domain.tld"));
        storage.createAccount(new Identity("bob", "bob@domain.tld"));
        storage.createAccount(new Identity("charlie", "charlie@domain.tld"));

        var accounts = storage.getAllAccounts();
        assertThat(accounts).hasSize(3);
    }

    @Test
    void get_account_by_id() {
        var bob = new Identity("bob", "bob@domain.tld");
        var account = storage.createAccount(bob);

        var saved = storage.getAccountById(account.id());
        assertThat(saved).isNotNull();
    }

    @Test
    void get_huddle_by_date() {
        var announce = new HuddleAnnounce("2023-04-18 AM", "Learning TDD");
        storage.scheduleHuddle(announce);

        var found = storage.getHuddleByDate("2023-04-18 AM");
        assertThat(found.title()).isEqualTo("Learning TDD");
    }

    @Test
    void get_huddle_by_id() {
        var announce = new HuddleAnnounce("2023-04-18 AM", "Learning TDD");
        var huddle = storage.scheduleHuddle(announce);

        var found = storage.getHuddleById(huddle.id());
        assertThat(found.title()).isEqualTo("Learning TDD");
    }

    @Test
    void get_all_huddles() {
        storage.scheduleHuddle(new HuddleAnnounce("2023-04-18 AM", "Learning TDD"));
        storage.scheduleHuddle(new HuddleAnnounce("2023-05-18 AM", "Learning Kotlin"));

        var huddles = storage.getAllHuddles();

        assertThat(huddles.stream().map(Huddle::title)).containsExactly("Learning TDD", "Learning Kotlin");
    }

    @Test
    void register_participant_to_huddle() {
        var learningTdd = storage.scheduleHuddle(new HuddleAnnounce("2023-04-18 AM", "Learning TDD"));
        var alice = storage.createAccount(new Identity("Alice", "alice@acme.corp"));
        var bob = storage.createAccount(new Identity("bob", "bob@domain.tld"));

        storage.registerParticipant(bob, learningTdd);

        var participants = storage.getParticipantsForHuddle(learningTdd);
        assertThat(participants.size()).isEqualTo(1);
        var bobAccount = participants.get(0).account();
        assertThat(bobAccount.username()).isEqualTo("bob");
    }

    void get_participation() {
        var learningTdd = storage.scheduleHuddle(new HuddleAnnounce("2023-04-18 AM", "Learning TDD"));
        var learningKotlin = storage.scheduleHuddle(new HuddleAnnounce("2023-04-18 AM", "Learning Kotlin"));

        var bob = storage.createAccount(new Identity("bob", "bob@domain.tld"));
        storage.registerParticipant(bob, learningTdd);

        var huddles = storage.attendedBy(bob);
        assertThat(huddles.stream().map(h -> h.title())).containsExactly("Learning TDD");
    }

}
