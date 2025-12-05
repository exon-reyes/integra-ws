package integra.security.handler;

import integra.security.AccesoRequest;
import integra.security.JwtUtil;
import integra.security.UserPrincipal;
import integra.security.dto.JWTResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginHandler {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JWTResponse login(AccesoRequest request) {
        log.info("Iniciando login para usuario: {}", request.username());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        String token = jwtUtil.generateToken(user);
        Set<String> uiPermissions = user.getPermissions().stream().collect(java.util.stream.Collectors.toSet());

        return new JWTResponse(token, "Login exitoso", uiPermissions);
    }
}
