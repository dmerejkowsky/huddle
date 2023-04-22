package info.dmerej.huddle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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
    void register_account_then_find_it() {
        var created = new Identity("bob", "bob@domain.tdl");
        controller.createAccount(created);

        var returned = controller.getAccountByUserName("bob");
        assertThat(returned).isEqualTo(created);
    }


}
