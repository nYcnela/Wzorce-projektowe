package ma.swiftrent.dto;

import lombok.Builder;
import lombok.Data;
import ma.swiftrent.entity.User;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String role;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
