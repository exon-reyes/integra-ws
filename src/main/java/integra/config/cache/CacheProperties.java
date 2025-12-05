package integra.config.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "integra.cache")
@Data
public class CacheProperties {

    private Map<String, CacheSpec> specs;

    @Data
    public static class CacheSpec {
        private Duration expireAfterWrite;
        private Duration expireAfterAccess;
        private Duration refreshAfterWrite;
        private Long maximumSize;
        private Integer initialCapacity;
        private Boolean recordStats;
        private Boolean weakKeys;
        private Boolean weakValues;
        private Boolean softValues;
    }
}