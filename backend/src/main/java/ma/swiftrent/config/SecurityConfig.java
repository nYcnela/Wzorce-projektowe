package ma.swiftrent.config;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Konfiguracja bezpieczeństwa Spring Security.
 * Definiuje reguły dostępu, filtr JWT oraz politykę sesji.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Konfiguruje łańcuch filtrów bezpieczeństwa.
     * 
     * @param http HttpSecurity do konfiguracji
     * @return Skonfigurowany SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Włącza obsługę CORS (korzysta z beana corsConfigurationSource)
                .cors(cors -> cors.configure(http))
                // Wyłącza CSRF (używam JWT - stateless)
                .csrf(csrf -> csrf.disable())
                
                // Konfiguracja autoryzacji żądań
                .authorizeHttpRequests(auth -> auth
                        // Publiczne endpointy - dostępne dla wszystkich
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll() // Dostęp do zdjęć
                        
                        // GET /api/cars - publiczny dostęp (przeglądanie samochodów)
                        .requestMatchers(HttpMethod.GET, "/api/cars/**").permitAll()
                        
                        // POST /api/cars - tylko dla ADMIN'a
                        .requestMatchers(HttpMethod.POST, "/api/cars/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/cars/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/cars/**").hasRole("ADMIN")
                        
                        // Endpointy wypożyczeń - tylko dla zalogowanych użytkowników
                        .requestMatchers("/api/rentals/**").authenticated()
                        
                        // Wszystkie inne żądania wymagają uwierzytelnienia
                        .anyRequest().authenticated()
                )
                
                // Polityka sesji - STATELESS (bez sesji, tylko JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                
                // Dodaje własny provider uwierzytelnienia
                .authenticationProvider(authenticationProvider)
                
                // Dodaje filtr JWT przed filtrem UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
