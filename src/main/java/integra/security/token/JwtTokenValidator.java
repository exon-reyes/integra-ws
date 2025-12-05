package integra.security.token;

import integra.security.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * JWT token validation implementation.
 * Follows SRP - Single responsibility for JWT validation logic.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenValidator implements TokenValidator {

    private final JwtUtil jwtUtil;

    @Override
    public TokenValidationResult validateToken(String token) {

        if (jwtUtil.isTokenExpired(token)) {
            log.info("Token expired");
            return new TokenValidationResult(TokenStatus.EXPIRED, "Token has expired");
        }

        if (jwtUtil.esVersionDeprecada(token)) {
            log.info("Token version deprecated");
            throw new ExpiredJwtException(null, null, "Token version deprecated");
        }

        log.debug("Token validation successful");
        return new TokenValidationResult(TokenStatus.VALID, "Token is valid");
    }
}