package integra.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponse {
    private String token;
    private String message;
    private Set<String> uiPermissions;

    public JWTResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public JWTResponse(String token) {
        this.token = token;
        this.message = "Token generado exitosamente";
    }
}
