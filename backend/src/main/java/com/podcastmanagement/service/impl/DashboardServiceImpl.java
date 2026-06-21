package com.podcastmanagement.service.impl;

import com.podcastmanagement.dto.response.DashboardResponse;
import com.podcastmanagement.dto.response.NotificationResponse;
import com.podcastmanagement.dto.response.RecordingSessionResponse;
import com.podcastmanagement.entity.Notification;
import com.podcastmanagement.entity.Podcast;
import com.podcastmanagement.entity.RecordingSession;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.enums.EpisodeStatus;
import com.podcastmanagement.enums.TaskStatus;
import com.podcastmanagement.exception.ResourceNotFoundException;
import com.podcastmanagement.repository.EpisodeRepository;
import com.podcastmanagement.repository.NotificationRepository;
import com.podcastmanagement.repository.PodcastRepository;
import com.podcastmanagement.repository.RecordingSessionRepository;
import com.podcastmanagement.repository.TaskRepository;
import com.podcastmanagement.repository.UserRepository;
import com.podcastmanagement.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final PodcastRepository podcastRepository;
    private final EpisodeRepository episodeRepository;
    private final RecordingSessionRepository recordingSessionRepository;
    private final TaskRepository taskRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public DashboardServiceImpl(PodcastRepository podcastRepository, EpisodeRepository episodeRepository,
                                RecordingSessionRepository recordingSessionRepository,
                                TaskRepository taskRepository, NotificationRepository notificationRepository,
                                UserRepository userRepository) {
        this.podcastRepository = podcastRepository;
        this.episodeRepository = episodeRepository;
        this.recordingSessionRepository = recordingSessionRepository;
        this.taskRepository = taskRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DashboardResponse getDashboardMetrics(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Long teamId = user.getTeam() != null ? user.getTeam().getId() : null;

        List<Podcast> podcasts;
        if (teamId != null) {
            podcasts = podcastRepository.findByTeamId(teamId);
        } else {
            podcasts = podcastRepository.findAll();
        }
        long totalPodcasts = podcasts.size();

        List<Long> podcastIds = podcasts.stream().map(Podcast::getId).toList();
        long totalEpisodes = 0;
        Map<String, Long> episodesByStatus = new HashMap<>();
        for (EpisodeStatus status : EpisodeStatus.values()) {
            episodesByStatus.put(status.name(), 0L);
        }

        for (Podcast p : podcasts) {
            List<com.podcastmanagement.entity.Season> seasons = p.getSeasons();
            for (com.podcastmanagement.entity.Season season : seasons) {
                List<com.podcastmanagement.entity.Episode> episodes = season.getEpisodes();
                totalEpisodes += episodes.size();
                for (com.podcastmanagement.entity.Episode ep : episodes) {
                    String statusName = ep.getStatus().name();
                    episodesByStatus.merge(statusName, 1L, Long::sum);
                }
            }
        }

        List<RecordingSessionResponse> upcomingRecordings = new ArrayList<>();
        if (teamId != null) {
            upcomingRecordings = recordingSessionRepository
                    .findByEpisode_Season_Podcast_TeamId(teamId).stream()
                    .filter(s -> s.getRecordingDate() != null && !s.getRecordingDate().isBefore(LocalDate.now()))
                    .map(this::toRecordingSessionResponse)
                    .toList();
        } else {
            upcomingRecordings = recordingSessionRepository
                    .findByRecordingDateAfter(LocalDate.now()).stream()
                    .map(this::toRecordingSessionResponse)
                    .toList();
        }

        long pendingTasks = 0;
        long completedTasks = 0;
        Map<Long, Long> tasksByUser = new HashMap<>();
        if (teamId != null) {
            List<com.podcastmanagement.entity.Task> teamTasks = taskRepository
                    .findByEpisode_Season_Podcast_TeamId(teamId);
            pendingTasks = teamTasks.stream()
                    .filter(t -> t.getStatus() == TaskStatus.PENDING).count();
            completedTasks = teamTasks.stream()
                    .filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
            List<User> teamUsers = userRepository.findByTeamId(teamId);
            for (User u : teamUsers) {
                tasksByUser.put(u.getId(), teamTasks.stream()
                        .filter(t -> t.getAssignedTo() != null && t.getAssignedTo().getId().equals(u.getId()))
                        .count());
            }
        } else {
            pendingTasks = taskRepository.countByStatus(TaskStatus.PENDING);
            completedTasks = taskRepository.countByStatus(TaskStatus.COMPLETED);
            List<User> users = userRepository.findAll();
            for (User u : users) {
                tasksByUser.put(u.getId(), taskRepository.countByAssignedToId(u.getId()));
            }
        }

        List<NotificationResponse> recentNotifications = notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId).stream()
                .limit(10)
                .map(this::toNotificationResponse)
                .toList();

        return new DashboardResponse(totalPodcasts, totalEpisodes, episodesByStatus, upcomingRecordings,
                pendingTasks, completedTasks, tasksByUser, recentNotifications);
    }

    private RecordingSessionResponse toRecordingSessionResponse(RecordingSession session) {
        return new RecordingSessionResponse(
                session.getId(),
                session.getRecordingDate(),
                session.getRecordingTime(),
                session.getMeetingLink(),
                session.getLocation(),
                session.getNotes(),
                session.getEpisode().getId(),
                session.getEpisode().getTitle()
        );
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
