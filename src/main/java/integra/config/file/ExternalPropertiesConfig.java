package integra.config.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource(value = "file:./integra-config/business.properties", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "business")
@Component
public class ExternalPropertiesConfig {

    private final Jornada jornada = new Jornada();

    @Getter
    @Setter
    public static class Jornada {
        private String corteNocturno;
        private String corteNormal;
    }
}