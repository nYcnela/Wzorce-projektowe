package ma.swiftrent.security.flyweight;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Tydzien 4, Wzorzec Flyweight 1
// Wielu uzytkownikow korzysta tylko z dwoch rol, dlatego wspolny profil roli moze byc wspoldzielony.
// Flyweight przechowuje niemutowalny stan roli: nazwe, authority i domyslne claims do tokenu.
public final class RoleProfileFlyweight {

    private final String roleName;
    private final List<GrantedAuthority> authorities;
    private final Map<String, Object> defaultClaims;

    RoleProfileFlyweight(String roleName, String authorityName) {
        this.roleName = roleName;
        this.authorities = List.of(new SimpleGrantedAuthority(authorityName));
        this.defaultClaims = Map.of("role", roleName);
    }

    public String getRoleName() {
        return roleName;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Map<String, Object> copyDefaultClaims() {
        return new HashMap<>(defaultClaims);
    }
}
// Koniec, Tydzien 4, Wzorzec Flyweight 1
