package integra.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Path matcher for public endpoints that should skip JWT filtering.
 * Follows SRP - Single responsibility for public path matching.
 * Follows OCP - Easy to extend with new public paths.
 */
@Component
@Slf4j
public class PublicPathMatcher implements FilterPathMatcher {

    private static final Set<String> PUBLIC_PATH_PREFIXES = Set.of(
            "/auth/",
            "/public/",
            "/actuator/"
    );

    @Override
    public boolean shouldSkipFiltering(HttpServletRequest request) {
        String path = request.getRequestURI();

        boolean shouldSkip = PUBLIC_PATH_PREFIXES.stream()
                .anyMatch(path::startsWith);

        if (shouldSkip) {
            log.debug("Skipping JWT filtering for public path: {}", path);
        }

        return shouldSkip;
    }
}