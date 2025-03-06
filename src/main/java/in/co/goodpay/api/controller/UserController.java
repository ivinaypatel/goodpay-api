package in.co.goodpay.api.controller;

import in.co.goodpay.api.dto.*;
import in.co.goodpay.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goodpay-api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // User Registration (Admin creates a user)
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(
            @Valid @RequestBody UserRequestDTO userRequestDTO,
            @RequestParam String createdBy) {

        UserResponseDTO createdUser = userService.createUser(userRequestDTO, createdBy);
        return ResponseEntity.ok(createdUser);
    }

    // User Login (Dummy Implementation - Usually handled with JWT)
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok("Login successful! (JWT implementation pending)");
    }

    // Get User by Mobile Number
    @GetMapping("/{mobileNumber}")
    public ResponseEntity<UserResponseDTO> getUserByMobile(@PathVariable String mobileNumber) {
        UserResponseDTO user = userService.getUserByMobile(mobileNumber);
        return ResponseEntity.ok(user);
    }

    // Get All Active Users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Update User
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable int id,
            @Valid @RequestBody UpdateUserDTO updateUserDTO,
            @RequestParam String modifiedBy) {

        UserResponseDTO updatedUser = userService.updateUser(id, updateUserDTO, modifiedBy);
        return ResponseEntity.ok(updatedUser);
    }

    // Soft Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeleteUser(@PathVariable int id, @RequestParam String modifiedBy) {
        userService.softDeleteUser(id, modifiedBy);
        return ResponseEntity.ok("User with ID " + id + " has been deactivated.");
    }
}
