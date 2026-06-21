package com.podcastmanagement.service;

import com.podcastmanagement.dto.request.ProfileRequest;
import com.podcastmanagement.dto.request.UserRequest;
import com.podcastmanagement.dto.response.UserResponse;
import com.podcastmanagement.enums.UserRole;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers(Long userId);

    List<UserResponse> getUsersByRole(UserRole role);

    UserResponse login(String email, String password);

    UserResponse updateProfile(Long userId, ProfileRequest request);

    UserResponse getProfile(Long userId);

    List<UserResponse> getUsersByTeamId(Long teamId);

}
