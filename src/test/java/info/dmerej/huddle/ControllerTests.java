package info.dmerej.huddle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

@SpringBootTest
class ControllerTests {
    private final SqlStorage storage = SqlStorageTests.inMemory();
    private Controller controller;

    @BeforeEach
    void setUp() {
        storage.reset();
        controller = new Controller(storage);
    }

    @Test
    void context_loads() {
    }

    @Test
    void register_account() {
        var exception = catchThrowableOfType(
            () -> controller.get("bob"),
            ResponseStatusException.class);
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        var account = new AccountCreationRequest("bob", "bob@domain.tdl");
        controller.createAccount(account);

        var found = controller.get("bob");
        assertThat(found.username()).isEqualTo("bob");
    }

    @Test
    void register_account_then_list_it() {
        var account = new AccountCreationRequest("bob", "bob@domain.tdl");
        controller.createAccount(account);

        var returned = controller.listAccounts();
        assertThat(returned).hasSize(1);
    }


}
