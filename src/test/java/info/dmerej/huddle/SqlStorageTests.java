package info.dmerej.huddle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


public class SqlStorageTests {
    private final SqlStorage storage;

    public SqlStorageTests() {
        storage = inMemory();
    }

    public static SqlStorage inMemory() {
        return new SqlStorage("jdbc:h2:mem:huddle");
    }


    @BeforeEach
    public void clean() {
        storage.reset();
    }

    @Test
    void get_all_accounts() {
        var allAccounts = storage.getAllAccounts();
        assertThat(allAccounts).isEmpty();
    }

    @Test
    void insert_account() throws SQLException {
        var account = new AccountCreationRequest("bob", "bob@domain.tld");
        storage.createAccount(account);

        var found = storage.getAccountByUserName("bob");
        assertThat(found.username()).isEqualTo("bob");
        assertThat(found.email()).isEqualTo("bob@domain.tld");
    }

    @Test
    void get_account_by_id() {
        var request = new AccountCreationRequest("bob", "bob@domain.tld");
        var account = storage.createAccount(request);

        var saved = storage.getAccountById(account.id());
        assertThat(saved).isNotNull();
    }

    @Test
    void get_huddle_by_date() {
        var request = new HuddleScheduleRequest("2023-04-18 AM", "Learning TDD");
        storage.scheduleHuddle(request);

        var found = storage.getHuddleByDate("2023-04-18 AM");
        assertThat(found.title()).isEqualTo("Learning TDD");
    }

    @Test
    void get_huddle_by_id() {
        var request = new HuddleScheduleRequest("2023-04-18 AM", "Learning TDD");
        var huddle = storage.scheduleHuddle(request);

        var found = storage.getHuddleById(huddle.id());
        assertThat(found.title()).isEqualTo("Learning TDD");
    }

    @Test
    void register_participant_to_huddle() {
        var learningTdd = storage.scheduleHuddle(new HuddleScheduleRequest("2023-04-18 AM", "Learning TDD"));
        var alice = storage.createAccount(new AccountCreationRequest("Alice", "alice@acme.corp"));
        var bob = storage.createAccount(new AccountCreationRequest("bob", "bob@domain.tld"));

        storage.registerParticipant(bob.id(), learningTdd.id());

        var users = storage.getParticipantsForHuddle(learningTdd.id());
        assertThat(users.size()).isEqualTo(1);
    }

}
