package com.podcastmanagement.controller;

import com.podcastmanagement.dto.request.TeamRegisterRequest;
import com.podcastmanagement.dto.response.ApiResponse;
import com.podcastmanagement.dto.response.TeamResponse;
import com.podcastmanagement.entity.Team;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.enums.UserRole;
import com.podcastmanagement.exception.ResourceNotFoundException;
import com.podcastmanagement.repository.TeamRepository;
import com.podcastmanagement.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamController(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerTeam(@Valid @RequestBody TeamRegisterRequest request) {
        if (teamRepository.existsByName(request.getTeamName())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Team name already exists"));
        }
        if (userRepository.existsByEmail(request.getAdminEmail())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Email already exists"));
        }

        Team team = new Team();
        team.setName(request.getTeamName());
        Team savedTeam = teamRepository.save(team);

        User admin = new User();
        admin.setName(request.getAdminName());
        admin.setEmail(request.getAdminEmail());
        admin.setPassword(request.getAdminPassword());
        admin.setRole(UserRole.ADMIN);
        admin.setTeam(savedTeam);
        userRepository.save(admin);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Team registered successfully", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getAllTeams(@RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Only admin can list teams"));
        }
        List<TeamResponse> response = teamRepository.findAll().stream()
                .map(t -> new TeamResponse(t.getId(), t.getName(), t.getDescription(), t.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Teams retrieved successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeamById(@PathVariable Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", id));
        TeamResponse response = new TeamResponse(team.getId(), team.getName(), team.getDescription(), team.getCreatedAt());
        return ResponseEntity.ok(ApiResponse.success("Team retrieved successfully", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(@RequestBody Team team,
                                                                  @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Only admin can create teams"));
        }
        if (teamRepository.existsByName(team.getName())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Team name already exists"));
        }
        Team savedTeam = teamRepository.save(team);
        TeamResponse response = new TeamResponse(savedTeam.getId(), savedTeam.getName(), savedTeam.getDescription(), savedTeam.getCreatedAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Team created successfully", response));
    }
}