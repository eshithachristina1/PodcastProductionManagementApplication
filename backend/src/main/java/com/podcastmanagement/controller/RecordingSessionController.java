package com.podcastmanagement.controller;

import com.podcastmanagement.dto.request.RecordingSessionRequest;
import com.podcastmanagement.dto.response.ApiResponse;
import com.podcastmanagement.dto.response.RecordingSessionResponse;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.enums.UserRole;
import com.podcastmanagement.service.RecordingSessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recording-sessions")
public class RecordingSessionController {

    private final RecordingSessionService recordingSessionService;

    public RecordingSessionController(RecordingSessionService recordingSessionService) {
        this.recordingSessionService = recordingSessionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RecordingSessionResponse>> createRecordingSession(@Valid @RequestBody RecordingSessionRequest request,
                                                                                         @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN && currentUser.getRole() != UserRole.PRODUCER && currentUser.getRole() != UserRole.HOST) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Not authorized"));
        }
        RecordingSessionResponse response = recordingSessionService.createRecordingSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Recording session created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RecordingSessionResponse>> updateRecordingSession(@PathVariable Long id, @Valid @RequestBody RecordingSessionRequest request,
                                                                                         @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN && currentUser.getRole() != UserRole.PRODUCER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Not authorized"));
        }
        RecordingSessionResponse response = recordingSessionService.updateRecordingSession(id, request);
        return ResponseEntity.ok(ApiResponse.success("Recording session updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecordingSession(@PathVariable Long id,
                                                                     @RequestAttribute("currentUser") User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Only admin can delete recording sessions"));
        }
        recordingSessionService.deleteRecordingSession(id);
        return ResponseEntity.ok(ApiResponse.success("Recording session deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RecordingSessionResponse>> getRecordingSessionById(@PathVariable Long id) {
        RecordingSessionResponse response = recordingSessionService.getRecordingSessionById(id);
        return ResponseEntity.ok(ApiResponse.success("Recording session retrieved successfully", response));
    }

    @GetMapping("/episode/{episodeId}")
    public ResponseEntity<ApiResponse<List<RecordingSessionResponse>>> getRecordingSessionsByEpisodeId(@PathVariable Long episodeId) {
        List<RecordingSessionResponse> response = recordingSessionService.getRecordingSessionsByEpisodeId(episodeId);
        return ResponseEntity.ok(ApiResponse.success("Recording sessions retrieved successfully", response));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<RecordingSessionResponse>>> getUpcomingRecordings(@RequestAttribute("currentUser") User currentUser) {
        List<RecordingSessionResponse> response = recordingSessionService.getUpcomingRecordings(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Upcoming recordings retrieved successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RecordingSessionResponse>>> getAllRecordingSessions(@RequestAttribute("currentUser") User currentUser) {
        List<RecordingSessionResponse> response = recordingSessionService.getAllRecordingSessions(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Recording sessions retrieved successfully", response));
    }
}