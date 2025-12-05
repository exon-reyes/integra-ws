package integra.security.controller;

import integra.security.AccesoRequest;
import integra.security.dto.JWTResponse;
import integra.security.handler.LoginHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class LoginController {

    private final LoginHandler loginHandler;

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody @Valid AccesoRequest request) {

        return ResponseEntity.ok(loginHandler.login(request));
    }

}