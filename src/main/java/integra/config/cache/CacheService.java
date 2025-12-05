package integra.config.cache;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {

    private final CacheManager cacheManager;

    public Map<String, CacheStatsDto> getCacheStats() {
        Map<String, CacheStatsDto> stats = new HashMap<>();
        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                try {
                    Object nativeCache = cache.getNativeCache();
                    if (nativeCache instanceof com.github.benmanes.caffeine.cache.Cache caffeineCache) {
                        CacheStats caffeineStats = caffeineCache.stats();
                        ConcurrentMap<?, ?> cacheMap = caffeineCache.asMap();
                        CacheStatsDto dto = new CacheStatsDto(
                                caffeineStats.hitCount(),
                                caffeineStats.missCount(),
                                caffeineStats.loadCount(),
                                caffeineStats.loadSuccessCount(),
                                caffeineStats.loadFailureCount(),
                                caffeineStats.totalLoadTime(),
                                caffeineStats.evictionCount(),
                                caffeineStats.hitRate(),
                                caffeineStats.missRate(),
                                cacheMap.size()
                        );
                        stats.put(cacheName, dto);
                    }
                } catch (Exception e) {
                    log.warn("Error getting stats for cache '{}': {}", cacheName, e.getMessage());
                }
            }
        });
        return stats;
    }

    public void clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            log.info("Cache '{}' cleared manually", cacheName);
        } else {
            log.warn("Cache '{}' not found", cacheName);
        }
    }

    public void clearAllCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                log.info("Cache '{}' cleared", cacheName);
            }
        });
        log.info("All caches cleared manually");
    }

    public record CacheStatsDto(
            long hitCount,
            long missCount,
            long loadCount,
            long loadSuccessCount,
            long loadFailureCount,
            long totalLoadTime,
            long evictionCount,
            double hitRate,
            double missRate,
            long size
    ) {
    }
}