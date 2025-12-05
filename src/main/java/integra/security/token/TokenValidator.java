package integra.security.token;

/**
 * Interface for JWT token validation operations.
 * Follows ISP - Segregated interface for token validation concerns.
 */
public interface TokenValidator {

    /**
     * Validates token and returns validation result.
     *
     * @param token JWT token to validate
     * @return TokenValidationResult containing status and details
     */
    TokenValidationResult validateToken(String token);

    /**
     * Possible token validation statuses.
     */
    enum TokenStatus {
        VALID,
        EXPIRED,
        REVOKED,
        INVALID,
        DEPRECATED
    }

    /**
     * Token validation result with status and optional details.
     */
    record TokenValidationResult(
            TokenStatus status,
            String message
    ) {
    }
}