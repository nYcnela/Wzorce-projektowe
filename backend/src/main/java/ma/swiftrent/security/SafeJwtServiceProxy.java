package ma.swiftrent.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// Tydzień 4, Wzorzec Proxy 3
// Operacje JWT przechodzą przez bezpiecznego pośrednika, który odrzuca oczywiście błędne dane wejściowe.
// Proxy chroni rzeczywisty serwis tokenów przed pustymi tokenami i niekontrolowanymi błędami parsowania.
@Service
@Primary
@RequiredArgsConstructor
public class SafeJwtServiceProxy implements TokenService {

    private static final int MIN_TOKEN_LENGTH = 20;

    @Qualifier("jwtServiceTarget")
    private final TokenService tokenService;

    @Override
    public String extractUsername(String token) {
        if (!looksLikeToken(token)) {
            return null;
        }

        try {
            return tokenService.extractUsername(token);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        validateUserDetails(userDetails);
        return tokenService.generateToken(userDetails);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        validateUserDetails(userDetails);
        Map<String, Object> sanitizedClaims = extraClaims == null ? new HashMap<>() : new HashMap<>(extraClaims);
        return tokenService.generateToken(sanitizedClaims, userDetails);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (!looksLikeToken(token) || userDetails == null) {
            return false;
        }

        try {
            return tokenService.isTokenValid(token, userDetails);
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private boolean looksLikeToken(String token) {
        return token != null && !token.isBlank() && token.length() >= MIN_TOKEN_LENGTH;
    }

    private void validateUserDetails(UserDetails userDetails) {
        if (userDetails == null || userDetails.getUsername() == null || userDetails.getUsername().isBlank()) {
            throw new RuntimeException("Nie mozna wygenerowac tokenu bez poprawnych danych uzytkownika");
        }
    }
}
// Koniec, Tydzień 4, Wzorzec Proxy 3
