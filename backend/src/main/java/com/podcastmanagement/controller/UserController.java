package com.podcastmanagement.controller;

import com.podcastmanagement.dto.request.ProfileRequest;
import com.podcastmanagement.dto.request.UserRequest;
import com.podcastmanagement.dto.response.ApiResponse;
import com.podcastmanagement.dto.response.UserResponse;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.enums.UserRole;
import com.podcastmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest request,
                                                                  @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Only admin can create users"));
        }
        if (currentUser.getTeam() != null) {
            request.setTeamName(currentUser.getTeam().getName());
        }
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User created successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody UserRequest request) {
        UserResponse response = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(@RequestAttribute("currentUser") User currentUser) {
        List<UserResponse> response = userService.getAllUsers(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id,
                                                                  @Valid @RequestBody UserRequest request,
                                                                  @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Only admin can update users"));
        }
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id,
                                                        @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Only admin can delete users"));
        }
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@RequestAttribute("currentUser") User currentUser) {
        UserResponse response = userService.getProfile(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", response));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@Valid @RequestBody ProfileRequest request,
                                                                     @RequestAttribute("currentUser") User currentUser) {
        UserResponse response = userService.updateProfile(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }

    @GetMapping("/team")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getTeamUsers(@RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getTeam() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("User does not belong to a team"));
        }
        List<UserResponse> response = userService.getUsersByTeamId(currentUser.getTeam().getId());
        return ResponseEntity.ok(ApiResponse.success("Team users retrieved successfully", response));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByRole(@PathVariable UserRole role) {
        List<UserResponse> response = userService.getUsersByRole(role);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", response));
    }
}