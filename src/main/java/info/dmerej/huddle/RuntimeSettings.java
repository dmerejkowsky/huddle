package info.dmerej.huddle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuntimeSettings {
    @Bean("storage")
    public SqlStorage getStorage() {
        // NOTE: keep this consistent with flyway.conf
        String url = "jdbc:h2:~/.cache/huddle.h2";
        return new SqlStorage(url);
    }
}
