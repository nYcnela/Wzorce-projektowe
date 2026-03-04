package ma.swiftrent.controller;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.CarResponse;
import ma.swiftrent.dto.UserResponse;
import ma.swiftrent.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/favorites/{carId}")
    public ResponseEntity<Void> toggleFavorite(@PathVariable Long carId, @AuthenticationPrincipal UserDetails userDetails) {
        userService.toggleFavorite(userDetails.getUsername(), carId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<CarResponse>> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserFavorites(userDetails.getUsername()));
    }
}
