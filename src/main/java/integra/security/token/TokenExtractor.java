package integra.security.token;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * Interface for extracting JWT tokens from HTTP requests.
 * Follows ISP - Single responsibility for token extraction.
 */
public interface TokenExtractor {

    /**
     * Extracts JWT token from Authorization header.
     *
     * @param request HTTP request containing Authorization header
     * @return Optional containing token if present and valid format, empty otherwise
     */
    Optional<String> extractToken(HttpServletRequest request);
}