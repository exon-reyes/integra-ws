package integra;

import integra.config.file.ExternalPropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(ExternalPropertiesConfig.class)
public class WsIntegraApplication {

    public static void main(String[] args) {
        SpringApplication.run(WsIntegraApplication.class, args);
    }

}
