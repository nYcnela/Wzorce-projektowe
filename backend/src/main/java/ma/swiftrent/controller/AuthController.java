package ma.swiftrent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.AuthResponse;
import ma.swiftrent.dto.LoginRequest;
import ma.swiftrent.dto.RegisterRequest;
import ma.swiftrent.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Kontroler obsługujący endpointy uwierzytelniania.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint rejestracji nowego użytkownika.
     *
     * @param request Dane rejestracyjne
     * @return Odpowiedź z tokenem JWT
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Endpoint logowania użytkownika.
     *
     * @param request Dane logowania
     * @return Odpowiedź z tokenem JWT
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
