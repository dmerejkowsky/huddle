package info.dmerej.huddle;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestSettings {
    @Bean("storage")
    public SqlStorage getStorage() {
        String url = "jdbc:h2:mem:h2";
        return new SqlStorage(url);
    }
}
