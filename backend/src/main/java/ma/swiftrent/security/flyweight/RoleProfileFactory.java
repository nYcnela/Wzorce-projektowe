package ma.swiftrent.security.flyweight;

import ma.swiftrent.entity.User;

import java.util.Map;

public final class RoleProfileFactory {

    private static final Map<User.Role, RoleProfileFlyweight> ROLE_PROFILES = Map.of(
            User.Role.USER, new RoleProfileFlyweight(User.Role.USER.name(), "ROLE_USER"),
            User.Role.ADMIN, new RoleProfileFlyweight(User.Role.ADMIN.name(), "ROLE_ADMIN")
    );

    private RoleProfileFactory() {
    }

    public static RoleProfileFlyweight forRole(User.Role role) {
        RoleProfileFlyweight profile = ROLE_PROFILES.get(role);
        if (profile == null) {
            throw new RuntimeException("Brak profilu flyweight dla roli " + role);
        }
        return profile;
    }
}
