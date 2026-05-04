package ma.swiftrent.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.swiftrent.pattern.prototype.Prototype;
import ma.swiftrent.security.flyweight.RoleProfileFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Encja reprezentująca użytkownika.
 * Implementuje UserDetails dla integracji z Spring Security.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
/*
Tydzień 9, Jasne i zrozumiałe nazwy 1
Nazwa "User" jednoznacznie oznacza użytkownika aplikacji,
nazwy "email, password, role" jednoznacznie oznaczają co te pola reprezentują,
podobnie jak nazwy metod
 */
public class User implements UserDetails, Prototype<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
//Koniec, Tydzień 9, Jasne i zrozumiałe nazwy 1
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Rental> rentals;

    @ManyToMany
    @JoinTable(
        name = "user_favorites",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "car_id")
    )
    private List<Car> favorites;

    /**
     * Zwraca uprawnienia użytkownika dla Spring Security.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return RoleProfileFactory.forRole(role).getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    /*
        Tydzień 2, Wzorzec Prototype 2
        Tworzy nowy obiekt klasy User poprzez
        kopiowanie istniejacej już instancji
        za pomocą konstruktora kopiujacego
    */
    public User(User source){
        this.email = source.email;
        this.password = source.password;
        this.role = source.role;
        this.rentals = source.rentals;
        this.favorites = source.favorites;
    }

    @Override
    public User clone() {
        User copy = new User(this);
        return copy;
    }
    //Koniec, Tydzień 2, Wzorzec Prototype 2

    /**
     * Enum reprezentujący role użytkowników w systemie.
     */
    public enum Role {
        USER,
        ADMIN
    }

    // Tydzień 2, Wzorzec Builder 2
    // Użytkownik ma pola obowiązkowe, domyślną rolę i kolekcje relacji.
    // Ręczny builder zastępuje builder Lomboka i pilnuje spójnego stanu nowego obiektu.
    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static final class UserBuilder {
        private Long id;
        private String email;
        private String password;
        private Role role = Role.USER;
        private List<Rental> rentals = new ArrayList<>();
        private List<Car> favorites = new ArrayList<>();

        private UserBuilder() {
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder userRole() {
            this.role = Role.USER;
            return this;
        }

        public UserBuilder adminRole() {
            this.role = Role.ADMIN;
            return this;
        }

        public UserBuilder rentals(List<Rental> rentals) {
            this.rentals = rentals;
            return this;
        }

        public UserBuilder favorites(List<Car> favorites) {
            this.favorites = favorites;
            return this;
        }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);
            user.setRentals(rentals);
            user.setFavorites(favorites);
            return user;
        }
    }
    // Koniec, Tydzień 2, Wzorzec Builder 2
}
