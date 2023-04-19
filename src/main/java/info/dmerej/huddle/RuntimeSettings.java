package info.dmerej.huddle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuntimeSettings {
    @Bean("storage")
    public SqlStorage getStorage() {
        String url = "jdbc:h2:filesystem:./hudde.h2";
        return new SqlStorage(url);
    }
}
