package ma.swiftrent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dla żądania logowania użytkownika.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email nie może być pusty")
    @Email(message = "Email musi być prawidłowy")
    private String email;

    @NotBlank(message = "Hasło nie może być puste")
    private String password;
}
