package com.podcastmanagement.controller;

import com.podcastmanagement.dto.response.ApiResponse;
import com.podcastmanagement.dto.response.NotificationResponse;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.service.NotificationService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(
            @RequestParam("title") String title,
            @RequestParam("message") String message,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestAttribute("currentUser") User currentUser) {
        NotificationResponse response = notificationService.createNotification(title, message, userId, currentUser.getId(), file);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Notification created successfully", response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationResponse> response = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Notifications retrieved successfully", response));
    }

    @GetMapping("/sent/{senderId}")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getSentNotifications(@PathVariable Long senderId) {
        List<NotificationResponse> response = notificationService.getSentNotifications(senderId);
        return ResponseEntity.ok(ApiResponse.success("Sent notifications retrieved successfully", response));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable Long id) {
        NotificationResponse response = notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", response));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long id) {
        Resource resource = notificationService.downloadAttachment(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@PathVariable Long userId) {
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(ApiResponse.success("Unread count retrieved successfully", count));
    }
}