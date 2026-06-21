package com.podcastmanagement.controller;

import com.podcastmanagement.dto.request.TaskRequest;
import com.podcastmanagement.dto.response.ApiResponse;
import com.podcastmanagement.dto.response.TaskResponse;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@Valid @RequestBody TaskRequest request,
                                                                 @RequestAttribute("currentUser") User currentUser) {
        request.setCreatedById(currentUser.getId());
        TaskResponse response = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Task created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request,
                                                                 @RequestAttribute("currentUser") User currentUser) {
        TaskResponse existing = taskService.getTaskById(id);
        boolean isCreator = existing.getCreatedById() != null && existing.getCreatedById().equals(currentUser.getId());
        boolean isAssignee = existing.getAssignedToId() != null && existing.getAssignedToId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        if (!isCreator && !isAssignee && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Not authorized"));
        }
        TaskResponse response = taskService.updateTask(id, request);
        return ResponseEntity.ok(ApiResponse.success("Task updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id,
                                                         @RequestAttribute("currentUser") User currentUser) {
        TaskResponse existing = taskService.getTaskById(id);
        boolean isCreator = existing.getCreatedById() != null && existing.getCreatedById().equals(currentUser.getId());
        boolean isAssignee = existing.getAssignedToId() != null && existing.getAssignedToId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        if (!isCreator && !isAssignee && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Not authorized"));
        }
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.success("Task deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable Long id) {
        TaskResponse response = taskService.getTaskById(id);
        return ResponseEntity.ok(ApiResponse.success("Task retrieved successfully", response));
    }

    @GetMapping("/episode/{episodeId}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByEpisodeId(@PathVariable Long episodeId) {
        List<TaskResponse> response = taskService.getTasksByEpisodeId(episodeId);
        return ResponseEntity.ok(ApiResponse.success("Tasks retrieved successfully", response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByUserId(@PathVariable Long userId) {
        List<TaskResponse> response = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Tasks retrieved successfully", response));
    }

    @PutMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<ApiResponse<TaskResponse>> assignTask(@PathVariable Long taskId, @PathVariable Long userId) {
        TaskResponse response = taskService.assignTask(taskId, userId);
        return ResponseEntity.ok(ApiResponse.success("Task assigned successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks(@RequestAttribute("currentUser") User currentUser) {
        List<TaskResponse> response = taskService.getAllTasks(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Tasks retrieved successfully", response));
    }
}