package info.dmerej.huddle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(TestSettings.class)
public class EndToEndTests {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
        MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        StandardCharsets.UTF_8
    );

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        try {
            doPost("/reset-db", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Scenario:
     *   Get account with id 1 (should not exist yet)
     *   Post a new account
     *   Get account with id 1 (should have been created)
     */
    @Test
    void create_account_then_find_it() throws Exception {
        doGet("/account/alice").andExpect(status().isNotFound());

        doPost("/account", """
            {
               "username": "alice",
               "email": "alice@domain.tld"
            }
            """).andExpect(status().isOk());

        doGet("/account/alice").andExpect(status().isOk());
    }

    private ResultActions doGet(String url) throws Exception {
        return mockMvc.perform(get(url));
    }

    private ResultActions doPost(String url, String json) throws Exception {
        return mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8)
            .content(json));
    }
}
