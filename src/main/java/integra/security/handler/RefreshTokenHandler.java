package integra.security.handler;

import integra.security.JwtUtil;
import integra.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RefreshTokenHandler {

    private final JwtUtil jwtUtil;

    public String refresh(String token) {
        // Validar token actual
        if (jwtUtil.isTokenExpired(token)) {
            throw new RuntimeException("El token ha expirado, requiere un nuevo inicio de sesión");
        }
        Claims claims = jwtUtil.extractAllClaims(token);
        // Extraer authorities del token (roles + permisos especiales mezclados)
        @SuppressWarnings("unchecked") List<String> authorities = (List<String>) claims.get("authorities");

        // Reconstruir el usuario desde los claims
        UserPrincipal user = new UserPrincipal(claims.get("id", Long.class), claims.getSubject(), null, // email no necesario para refresh
                null, // fullname no necesario
                null, // password no necesario
                Set.of(), // authorities se reconstruirán en generateToken
                claims.get("enabled", Boolean.class), null, // roles no necesarios para refresh
                null, // permisos completos no necesarios
                null, // permisos especiales no necesarios
                authorities, // authorities originales del JWT
                claims.get("ver", Integer.class), claims.get("sup", Boolean.class), claims.get("empleadoId", Integer.class));

        // Generar nuevo token
        return jwtUtil.generateToken(user);
    }
}
