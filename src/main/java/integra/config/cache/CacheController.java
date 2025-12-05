package integra.config.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/Admin/cache")
@RequiredArgsConstructor
@Slf4j
public class CacheController {

    private final CacheService cacheService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, CacheService.CacheStatsDto>> getStats() {
        log.info("Retrieving cache statistics");
        return ResponseEntity.ok(cacheService.getCacheStats());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAll() {
        log.info("Clearing all caches");
        cacheService.clearAllCaches();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear/{cacheName}")
    public ResponseEntity<Void> clearCache(@PathVariable String cacheName) {
        log.info("Clearing cache: {}", cacheName);
        cacheService.clearCache(cacheName);
        return ResponseEntity.noContent().build();
    }
}