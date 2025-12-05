package integra.security;

import integra.seguridad.usuario.service.TokenVersionService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {
    private final TokenVersionService tokenVersionService;
    @Value("${security.jwt.private-key}")
    private RSAPrivateKey privateKey;

    @Value("${security.jwt.public-key}")
    private RSAPublicKey publicKey;

    @Value("${security.jwt.expiration}")
    private Long jwtExpirationInSeconds;

    public String generateToken(UserPrincipal user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        if (user.getEsSupervisor() != null) {
            claims.put("sup", user.getEsSupervisor());
        }
        if (user.getEmpleadoId() != null) {
            claims.put("empleadoId", user.getEmpleadoId());
        }
        claims.put("name", user.getFullname());
        claims.put("authorities", user.getAuthoritiesForToken()); // roles + permisos especiales únicamente
        claims.put("enabled", user.isEnabled());
        claims.put("ver", tokenVersionService.getVersion(user.getUsername()));
        return createToken(claims, user.getUsername(), jwtExpirationInSeconds);
    }

    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .id(UUID.randomUUID().toString())
                .issuer("integra-auth-server")
                .signWith(privateKey)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean esVersionDeprecada(String token) {
        try {
            // Extrae todos los claims del token
            Claims claims = extractAllClaims(token);

            // Obtiene el usuario y la versión del token
            String username = claims.getSubject();
            Integer tokenVersion = claims.get("ver", Integer.class);

            // Obtiene la versión actual almacenada (cache o DB)
            int currentVersion = tokenVersionService.getVersion(username);

            // Si la versión del token es menor que la actual, está deprecado
            return tokenVersion == null || tokenVersion < currentVersion;

        } catch (JwtException e) {
            // Si hay algún error en el token, se considera inválido/deprecado
            log.warn("Error verificando versión del token: {}", e.getMessage());
            return true;
        }
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()             // JJWT 0.13.0
                    .verifyWith(publicKey)   // RSAPublicKey -> verifica RS256
                    .requireIssuer("integra-auth-server").build().parseSignedClaims(token).getPayload();
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("Firma de token inválida: {}", e.getMessage());
            throw e;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("Token expirado: {}", e.getMessage());
            throw e;
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            log.error("Token malformado: {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            log.error("Error procesando JWT: {}", e.getMessage());
            throw e;
        }
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
