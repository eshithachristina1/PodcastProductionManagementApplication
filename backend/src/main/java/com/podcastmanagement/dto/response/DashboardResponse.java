package com.podcastmanagement.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardResponse {

    private long totalPodcasts;
    private long totalEpisodes;
    private Map<String, Long> episodesByStatus;
    private List<RecordingSessionResponse> upcomingRecordings;
    private long pendingTasks;
    private long completedTasks;
    private Map<Long, Long> tasksByUser;
    private List<NotificationResponse> recentNotifications;
    private Long teamId;

    public DashboardResponse() {}

    public DashboardResponse(long totalPodcasts, long totalEpisodes, Map<String, Long> episodesByStatus,
                             List<RecordingSessionResponse> upcomingRecordings, long pendingTasks,
                             long completedTasks, Map<Long, Long> tasksByUser,
                             List<NotificationResponse> recentNotifications) {
        this.totalPodcasts = totalPodcasts;
        this.totalEpisodes = totalEpisodes;
        this.episodesByStatus = episodesByStatus;
        this.upcomingRecordings = upcomingRecordings;
        this.pendingTasks = pendingTasks;
        this.completedTasks = completedTasks;
        this.tasksByUser = tasksByUser;
        this.recentNotifications = recentNotifications;
    }

    public long getTotalPodcasts() { return totalPodcasts; }
    public void setTotalPodcasts(long totalPodcasts) { this.totalPodcasts = totalPodcasts; }
    public long getTotalEpisodes() { return totalEpisodes; }
    public void setTotalEpisodes(long totalEpisodes) { this.totalEpisodes = totalEpisodes; }
    public Map<String, Long> getEpisodesByStatus() { return episodesByStatus; }
    public void setEpisodesByStatus(Map<String, Long> episodesByStatus) { this.episodesByStatus = episodesByStatus; }
    public List<RecordingSessionResponse> getUpcomingRecordings() { return upcomingRecordings; }
    public void setUpcomingRecordings(List<RecordingSessionResponse> upcomingRecordings) { this.upcomingRecordings = upcomingRecordings; }
    public long getPendingTasks() { return pendingTasks; }
    public void setPendingTasks(long pendingTasks) { this.pendingTasks = pendingTasks; }
    public long getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(long completedTasks) { this.completedTasks = completedTasks; }
    public Map<Long, Long> getTasksByUser() { return tasksByUser; }
    public void setTasksByUser(Map<Long, Long> tasksByUser) { this.tasksByUser = tasksByUser; }
    public List<NotificationResponse> getRecentNotifications() { return recentNotifications; }
    public void setRecentNotifications(List<NotificationResponse> recentNotifications) { this.recentNotifications = recentNotifications; }
    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }
}
