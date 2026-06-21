package com.podcastmanagement.dto.response;

import java.time.LocalDateTime;

public class PodcastResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private String hostName;
    private LocalDateTime createdAt;
    private int seasonCount;
    private Long teamId;

    public PodcastResponse() {}

    public PodcastResponse(Long id, String title, String description, String category, String hostName,
                           LocalDateTime createdAt, int seasonCount, Long teamId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.hostName = hostName;
        this.createdAt = createdAt;
        this.seasonCount = seasonCount;
        this.teamId = teamId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getHostName() { return hostName; }
    public void setHostName(String hostName) { this.hostName = hostName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public int getSeasonCount() { return seasonCount; }
    public void setSeasonCount(int seasonCount) { this.seasonCount = seasonCount; }
    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }
}
