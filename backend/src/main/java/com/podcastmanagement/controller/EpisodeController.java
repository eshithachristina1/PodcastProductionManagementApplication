package com.podcastmanagement.controller;

import com.podcastmanagement.dto.request.EpisodeRequest;
import com.podcastmanagement.dto.response.ApiResponse;
import com.podcastmanagement.dto.response.EpisodeResponse;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.enums.EpisodeStatus;
import com.podcastmanagement.enums.UserRole;
import com.podcastmanagement.service.EpisodeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/episodes")
public class EpisodeController {

    private final EpisodeService episodeService;

    public EpisodeController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EpisodeResponse>> createEpisode(@Valid @RequestBody EpisodeRequest request,
                                                                       @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN && currentUser.getRole() != UserRole.HOST && currentUser.getRole() != UserRole.WRITER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Not authorized"));
        }
        EpisodeResponse response = episodeService.createEpisode(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Episode created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EpisodeResponse>> updateEpisode(@PathVariable Long id, @Valid @RequestBody EpisodeRequest request,
                                                                       @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN && currentUser.getRole() != UserRole.HOST && currentUser.getRole() != UserRole.EDITOR && currentUser.getRole() != UserRole.WRITER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Not authorized"));
        }
        EpisodeResponse response = episodeService.updateEpisode(id, request);
        return ResponseEntity.ok(ApiResponse.success("Episode updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEpisode(@PathVariable Long id,
                                                            @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Only admin can delete episodes"));
        }
        episodeService.deleteEpisode(id);
        return ResponseEntity.ok(ApiResponse.success("Episode deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EpisodeResponse>> getEpisodeById(@PathVariable Long id) {
        EpisodeResponse response = episodeService.getEpisodeById(id);
        return ResponseEntity.ok(ApiResponse.success("Episode retrieved successfully", response));
    }

    @GetMapping("/season/{seasonId}")
    public ResponseEntity<ApiResponse<List<EpisodeResponse>>> getEpisodesBySeasonId(@PathVariable Long seasonId) {
        List<EpisodeResponse> response = episodeService.getEpisodesBySeasonId(seasonId);
        return ResponseEntity.ok(ApiResponse.success("Episodes retrieved successfully", response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<EpisodeResponse>> changeEpisodeStatus(@PathVariable Long id, @RequestBody EpisodeStatus status) {
        EpisodeResponse response = episodeService.changeEpisodeStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Episode status changed successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EpisodeResponse>>> getAllEpisodes(@RequestAttribute("currentUser") User currentUser) {
        List<EpisodeResponse> response = episodeService.getAllEpisodes(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Episodes retrieved successfully", response));
    }
}