package ma.swiftrent.service;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.AuthResponse;
import ma.swiftrent.dto.LoginRequest;
import ma.swiftrent.dto.RegisterRequest;
import ma.swiftrent.entity.User;
import ma.swiftrent.pattern.prototype.UserPrototypeRegistry;
import ma.swiftrent.repository.UserRepository;
import ma.swiftrent.security.TokenService;
import ma.swiftrent.security.flyweight.RoleProfileFactory;
import ma.swiftrent.service.logger.AppLogger;
import ma.swiftrent.service.logger.Slf4jLoggerAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Serwis obsługujący uwierzytelnianie i rejestrację użytkowników.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    AppLogger logger = new Slf4jLoggerAdapter(AuthService.class);

    /**
     * Rejestruje nowego użytkownika w systemie.
     *
     * @param request Dane rejestracyjne użytkownika
     * @return Odpowiedź z tokenem JWT
     * @throws RuntimeException gdy użytkownik o danym emailu już istnieje
     */
    public AuthResponse register(RegisterRequest request) {
        // Sprawdza czy użytkownik już istnieje
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Użytkownik o podanym emailu już istnieje");
        }

        // Tworzy nowego użytkownika
//        var user = User.builder()
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .userRole()
//                .build();
        /*
        Tydzień 2, Wzorzec Prototype 2
        Wykorzystanie schematu użytkwonika z rejetru prototypów
        do stworzenia nowego użytkownika podczas rejestracji
         */
        var user = UserPrototypeRegistry.getPrototype("user-template");
        if (user == null) {
            throw new RuntimeException("Prototyp użytkownika nie został zarejestrowany");
        }

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        //Koniec, Tydzień 2, Wzorzec Prototype 2

        logger.logInfo("Rejestracja użytkownika");
        userRepository.save(user);

        // Generuje token JWT z rolą
        var jwtToken = tokenService.generateToken(buildRoleClaims(user), user);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    /**
     * Loguje użytkownika do systemu.
     *
     * @param request Dane logowania użytkownika
     * @return Odpowiedź z tokenem JWT
     */
    public AuthResponse login(LoginRequest request) {
        // Uwierzytelnia użytkownika
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Znajduje użytkownika w bazie
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Użytkownik nie został znaleziony"));

        // Generuje token JWT z rolą
        var jwtToken = tokenService.generateToken(buildRoleClaims(user), user);

        logger.logInfo("Logowanie użytkownika");

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    private Map<String, Object> buildRoleClaims(User user) {
        return RoleProfileFactory.forRole(user.getRole()).copyDefaultClaims();
    }
}
