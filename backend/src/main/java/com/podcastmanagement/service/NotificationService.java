package com.podcastmanagement.service;

import com.podcastmanagement.dto.response.NotificationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(String title, String message, Long userId, Long senderId, MultipartFile file);

    List<NotificationResponse> getNotificationsByUserId(Long userId);

    List<NotificationResponse> getSentNotifications(Long senderId);

    NotificationResponse markAsRead(Long id);

    long getUnreadCount(Long userId);

    org.springframework.core.io.Resource downloadAttachment(Long id);

}
