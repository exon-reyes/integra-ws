package integra.security.controller;

import integra.security.dto.JWTResponse;
import integra.security.handler.RefreshTokenHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/refresh")
public class RefreshTokenController {
    private final RefreshTokenHandler refreshTokenHandler;

    @PostMapping("/refresh-token")
    public ResponseEntity<JWTResponse> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(new JWTResponse(null, "Falta header Authorization Bearer"));
        }

        String token = authHeader.substring(7);
        try {
            String newToken = refreshTokenHandler.refresh(token);
            return ResponseEntity.ok(new JWTResponse(newToken));
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(new JWTResponse(null, "Token inválido o expirado: " + e.getMessage()));
        }
    }
}
