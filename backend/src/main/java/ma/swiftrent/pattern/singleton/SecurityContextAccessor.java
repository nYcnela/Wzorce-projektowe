package ma.swiftrent.pattern.singleton;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// Tydzień 2, Wzorzec Singleton 3 (Thread-safe synchronized)
// Dostęp do kontekstu bezpieczeństwa powinien być wykonywany w jednym miejscu,
// bo wiele serwisów korzysta z informacji o zalogowanym użytkowniku i jego rolach.
// Singleton zastępuje powtarzające się odwołania do SecurityContextHolder.
public final class SecurityContextAccessor {

    private static SecurityContextAccessor instance;

    private SecurityContextAccessor() {
    }

    public static synchronized SecurityContextAccessor getInstance() {
        if (instance == null) {
            instance = new SecurityContextAccessor();
        }
        return instance;
    }

    public String getCurrentUserEmail() {
        return getAuthentication().getName();
    }

    public boolean currentUserHasRole(String roleName) {
        return getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + roleName));
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("Brak zalogowanego użytkownika w kontekście bezpieczeństwa");
        }
        return authentication;
    }
}
// Koniec, Tydzień 2, Wzorzec Singleton 3
