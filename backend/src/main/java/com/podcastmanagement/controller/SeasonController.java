package com.podcastmanagement.controller;

import com.podcastmanagement.dto.request.SeasonRequest;
import com.podcastmanagement.dto.response.ApiResponse;
import com.podcastmanagement.dto.response.SeasonResponse;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.enums.UserRole;
import com.podcastmanagement.service.SeasonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seasons")
public class SeasonController {

    private final SeasonService seasonService;

    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SeasonResponse>> createSeason(@Valid @RequestBody SeasonRequest request,
                                                                     @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN && currentUser.getRole() != UserRole.HOST && currentUser.getRole() != UserRole.PRODUCER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Not authorized"));
        }
        SeasonResponse response = seasonService.createSeason(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Season created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SeasonResponse>> updateSeason(@PathVariable Long id, @Valid @RequestBody SeasonRequest request,
                                                                     @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN && currentUser.getRole() != UserRole.HOST) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Not authorized"));
        }
        SeasonResponse response = seasonService.updateSeason(id, request);
        return ResponseEntity.ok(ApiResponse.success("Season updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSeason(@PathVariable Long id,
                                                           @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Only admin can delete seasons"));
        }
        seasonService.deleteSeason(id);
        return ResponseEntity.ok(ApiResponse.success("Season deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SeasonResponse>> getSeasonById(@PathVariable Long id) {
        SeasonResponse response = seasonService.getSeasonById(id);
        return ResponseEntity.ok(ApiResponse.success("Season retrieved successfully", response));
    }

    @GetMapping("/podcast/{podcastId}")
    public ResponseEntity<ApiResponse<List<SeasonResponse>>> getSeasonsByPodcastId(@PathVariable Long podcastId) {
        List<SeasonResponse> response = seasonService.getSeasonsByPodcastId(podcastId);
        return ResponseEntity.ok(ApiResponse.success("Seasons retrieved successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SeasonResponse>>> getAllSeasons(@RequestAttribute("currentUser") User currentUser) {
        List<SeasonResponse> response = seasonService.getAllSeasons(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Seasons retrieved successfully", response));
    }
}