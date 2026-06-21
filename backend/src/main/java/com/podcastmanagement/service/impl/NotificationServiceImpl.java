package com.podcastmanagement.service.impl;

import com.podcastmanagement.dto.response.NotificationResponse;
import com.podcastmanagement.entity.Notification;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.exception.ResourceNotFoundException;
import com.podcastmanagement.repository.NotificationRepository;
import com.podcastmanagement.repository.UserRepository;
import com.podcastmanagement.service.NotificationService;
import com.podcastmanagement.util.FileUploadUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final FileUploadUtil fileUploadUtil;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   UserRepository userRepository, FileUploadUtil fileUploadUtil) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.fileUploadUtil = fileUploadUtil;
    }

    @Override
    public NotificationResponse createNotification(String title, String message, Long userId, Long senderId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setIsRead(false);
        notification.setUser(user);
        if (senderId != null) {
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", senderId));
            notification.setSender(sender);
        }
        if (file != null && !file.isEmpty()) {
            try {
                String filePath = fileUploadUtil.uploadFile(file);
                notification.setFilePath(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload attachment: " + e.getMessage(), e);
            }
        }
        Notification savedNotification = notificationRepository.save(notification);
        return toNotificationResponse(savedNotification);
    }

    @Override
    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toNotificationResponse)
                .toList();
    }

    @Override
    public List<NotificationResponse> getSentNotifications(Long senderId) {
        return notificationRepository.findBySenderIdOrderByCreatedAtDesc(senderId).stream()
                .map(this::toNotificationResponse)
                .toList();
    }

    @Override
    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", id));
        notification.setIsRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return toNotificationResponse(updatedNotification);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsRead(userId, false);
    }

    @Override
    public Resource downloadAttachment(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", id));
        if (notification.getFilePath() == null || notification.getFilePath().isEmpty()) {
            throw new ResourceNotFoundException("Attachment", id);
        }
        try {
            Path filePath = fileUploadUtil.getUploadPath().resolve(notification.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not found or not readable: " + notification.getFilePath());
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage(), e);
        }
    }

    private NotificationResponse toNotificationResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getIsRead(),
                notification.getCreatedAt(),
                notification.getUser().getId(),
                notification.getUser().getName(),
                notification.getSender() != null ? notification.getSender().getId() : null,
                notification.getSender() != null ? notification.getSender().getName() : null,
                notification.getFilePath()
        );
    }
}
