package info.dmerej.huddle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuntimeConfiguration {
    @Bean("storage")
    public Storage getStorage() {
        String url = "jdbc:sqlite:huddle.sqlite";
        return new Storage(url);
    }
}
