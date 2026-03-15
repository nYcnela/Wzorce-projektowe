package ma.swiftrent.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Serwis obsługujący operacje na tokenach JWT.
 * Odpowiedzialny za generowanie, walidację i ekstrakcję danych z tokenów.
 */
@Service("jwtServiceTarget")
public class JwtService implements TokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Ekstrahuje nazwę użytkownika (email) z tokenu JWT.
     *
     * @param token Token JWT
     * @return Nazwa użytkownika (email)
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Ekstrahuje konkretne roszczenie (claim) z tokenu.
     *
     * @param token Token JWT
     * @param claimsResolver Funkcja do ekstrakcji konkretnego roszczenia
     * @return Wartość roszczenia
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generuje token JWT dla użytkownika.
     *
     * @param userDetails Szczegóły użytkownika
     * @return Wygenerowany token JWT
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generuje token JWT z dodatkowymi roszczeniami.
     *
     * @param extraClaims Dodatkowe roszczenia do umieszczenia w tokenie
     * @param userDetails Szczegóły użytkownika
     * @return Wygenerowany token JWT
     */
    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Buduje token JWT.
     *
     * @param extraClaims Dodatkowe roszczenia
     * @param userDetails Szczegóły użytkownika
     * @param expiration Czas wygaśnięcia tokenu w milisekundach
     * @return Wygenerowany token JWT
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Sprawdza czy token jest ważny dla danego użytkownika.
     *
     * @param token Token JWT do walidacji
     * @param userDetails Szczegóły użytkownika
     * @return true jeśli token jest ważny, false w przeciwnym wypadku
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Sprawdza czy token wygasł.
     *
     * @param token Token JWT
     * @return true jeśli token wygasł, false w przeciwnym wypadku
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Ekstrahuje datę wygaśnięcia tokenu.
     *
     * @param token Token JWT
     * @return Data wygaśnięcia tokenu
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Ekstrahuje wszystkie roszczenia z tokenu.
     *
     * @param token Token JWT
     * @return Wszystkie roszczenia zawarte w tokenie
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Pobiera klucz do podpisywania tokenów.
     *
     * @return Klucz kryptograficzny
     */
    private javax.crypto.SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
