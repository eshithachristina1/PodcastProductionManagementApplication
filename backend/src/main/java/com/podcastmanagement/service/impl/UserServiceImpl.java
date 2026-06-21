package com.podcastmanagement.service.impl;

import com.podcastmanagement.dto.request.ProfileRequest;
import com.podcastmanagement.dto.request.UserRequest;
import com.podcastmanagement.dto.response.UserResponse;
import com.podcastmanagement.entity.Team;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.enums.UserRole;
import com.podcastmanagement.exception.DuplicateResourceException;
import com.podcastmanagement.exception.ResourceNotFoundException;
import com.podcastmanagement.repository.TeamRepository;
import com.podcastmanagement.repository.UserRepository;
import com.podcastmanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public UserServiceImpl(UserRepository userRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        if (request.getTeamName() != null && !request.getTeamName().isEmpty()) {
            Team team = teamRepository.findByName(request.getTeamName())
                    .orElseGet(() -> {
                        Team newTeam = new Team();
                        newTeam.setName(request.getTeamName());
                        return teamRepository.save(newTeam);
                    });
            user.setTeam(team);
        }
        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        if (request.getTeamName() != null && !request.getTeamName().isEmpty()) {
            Team team = teamRepository.findByName(request.getTeamName())
                    .orElseGet(() -> {
                        Team newTeam = new Team();
                        newTeam.setName(request.getTeamName());
                        return teamRepository.save(newTeam);
                    });
            user.setTeam(team);
        }
        User updatedUser = userRepository.save(user);
        return toUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        userRepository.delete(user);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return toUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers(Long userId) {
        User requestingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (requestingUser.getTeam() != null) {
            return userRepository.findByTeamId(requestingUser.getTeam().getId()).stream()
                    .map(this::toUserResponse)
                    .toList();
        }
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .toList();
    }

    @Override
    public List<UserResponse> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role).stream()
                .map(this::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        if (!user.getPassword().equals(password)) {
            throw new ResourceNotFoundException("Invalid email or password");
        }
        return toUserResponse(user);
    }

    @Override
    public UserResponse updateProfile(Long userId, ProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getNotableWork() != null) {
            user.setNotableWork(request.getNotableWork());
        }
        if (request.getExperience() != null) {
            user.setExperience(request.getExperience());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }
        User updatedUser = userRepository.save(user);
        return toUserResponse(updatedUser);
    }

    @Override
    public UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return toUserResponse(user);
    }

    @Override
    public List<UserResponse> getUsersByTeamId(Long teamId) {
        return userRepository.findByTeamId(teamId).stream()
                .map(this::toUserResponse)
                .toList();
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getNotableWork(),
                user.getExperience(),
                user.getPhone(),
                user.getProfilePicture(),
                user.getTeam() != null ? user.getTeam().getId() : null,
                user.getTeam() != null ? user.getTeam().getName() : null,
                user.getCreatedAt()
        );
    }
}
