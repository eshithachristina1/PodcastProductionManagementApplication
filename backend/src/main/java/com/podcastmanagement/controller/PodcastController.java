package com.podcastmanagement.controller;

import com.podcastmanagement.dto.request.PodcastRequest;
import com.podcastmanagement.dto.response.ApiResponse;
import com.podcastmanagement.dto.response.PodcastResponse;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.enums.UserRole;
import com.podcastmanagement.service.PodcastService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/podcasts")
public class PodcastController {

    private final PodcastService podcastService;

    public PodcastController(PodcastService podcastService) {
        this.podcastService = podcastService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PodcastResponse>> createPodcast(@Valid @RequestBody PodcastRequest request,
                                                                       @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN && currentUser.getRole() != UserRole.HOST && currentUser.getRole() != UserRole.PRODUCER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Not authorized to create podcasts"));
        }
        PodcastResponse response = podcastService.createPodcast(request, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Podcast created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PodcastResponse>> updatePodcast(@PathVariable Long id, @Valid @RequestBody PodcastRequest request,
                                                                        @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN && currentUser.getRole() != UserRole.HOST) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Not authorized to update podcasts"));
        }
        PodcastResponse response = podcastService.updatePodcast(id, request);
        return ResponseEntity.ok(ApiResponse.success("Podcast updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePodcast(@PathVariable Long id,
                                                            @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Only admin can delete podcasts"));
        }
        podcastService.deletePodcast(id);
        return ResponseEntity.ok(ApiResponse.success("Podcast deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PodcastResponse>> getPodcastById(@PathVariable Long id) {
        PodcastResponse response = podcastService.getPodcastById(id);
        return ResponseEntity.ok(ApiResponse.success("Podcast retrieved successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PodcastResponse>>> getAllPodcasts(@RequestAttribute("currentUser") User currentUser) {
        List<PodcastResponse> response = podcastService.getAllPodcasts(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Podcasts retrieved successfully", response));
    }
}