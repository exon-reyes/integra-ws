package integra.security.token;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation for extracting Bearer tokens from Authorization header.
 * Follows SRP - Single responsibility for Bearer token extraction logic.
 */
@Component
@Slf4j
public class BearerTokenExtractor implements TokenExtractor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = 7;

    @Override
    public Optional<String> extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.debug("No Bearer token found in Authorization header for URI: {}", request.getRequestURI());
            return Optional.empty();
        }

        String token = authHeader.substring(BEARER_PREFIX_LENGTH);
        return Optional.of(token);
    }
}