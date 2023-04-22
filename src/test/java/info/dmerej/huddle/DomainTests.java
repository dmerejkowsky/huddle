package info.dmerej.huddle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DomainTests {
    private FakeStorage storage;

    @BeforeEach
    public void setUp() {
        storage = new FakeStorage();
        storage.reset();
    }

    @Test
    void register_account_then_find_it() {
        var createdAccount = storage.createAccount(new Identity("bob", "bob@domain.tld"));

        var stored = storage.getAccountByUserName("bob");
        assertThat(stored).isEqualTo(createdAccount);
    }
}
