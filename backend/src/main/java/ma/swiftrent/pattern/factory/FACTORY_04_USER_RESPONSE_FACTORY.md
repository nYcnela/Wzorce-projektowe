# UserResponseFactory

## Rola
Konwertuje encję `User` do DTO `UserResponse`.

## Co robi
Implementuje `create(User entity)` z `ResponseFactory<User, UserResponse>`.

## Wejście i wyjście
- Wejście: `User`.
- Wyjście: `UserResponse`.

## Krok po kroku
1. `UserService` pobiera encje użytkowników.
2. Wywołuje `userResponseFactory.createAll(...)`.
3. Dla każdego `User` wywoływane jest `create(user)`.
4. Powstaje lista `UserResponse`.

## Czego używa
- `UserResponse.fromEntity(...)`.
- Mechanizmu generycznego `ResponseFactory`.

## Gdzie jest używana
- `UserService.getAllUsers()`.
