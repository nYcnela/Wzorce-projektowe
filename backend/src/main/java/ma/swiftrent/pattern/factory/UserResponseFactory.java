package ma.swiftrent.pattern.factory;

import ma.swiftrent.dto.UserResponse;
import ma.swiftrent.entity.User;
import org.springframework.stereotype.Component;

// Tydzień 3, Wzorzec Factory Method 1 – ConcreteCreator dla pary User → UserResponse
@Component
public class UserResponseFactory extends ResponseFactory<User, UserResponse> {

    @Override
    public UserResponse create(User entity) {
        return UserResponse.fromEntity(entity);
    }
}
