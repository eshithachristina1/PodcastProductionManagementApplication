package com.podcastmanagement.dto.response;

import com.podcastmanagement.enums.EpisodeStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EpisodeResponse {

    private Long id;
    private Integer episodeNumber;
    private String title;
    private String description;
    private LocalDate publishDate;
    private Integer duration;
    private EpisodeStatus status;
    private LocalDateTime createdAt;
    private Long seasonId;
    private String seasonTitle;
    private int taskCount;
    private int assetCount;

    public EpisodeResponse() {}

    public EpisodeResponse(Long id, Integer episodeNumber, String title, String description, LocalDate publishDate,
                           Integer duration, EpisodeStatus status, LocalDateTime createdAt, Long seasonId,
                           String seasonTitle, int taskCount, int assetCount) {
        this.id = id;
        this.episodeNumber = episodeNumber;
        this.title = title;
        this.description = description;
        this.publishDate = publishDate;
        this.duration = duration;
        this.status = status;
        this.createdAt = createdAt;
        this.seasonId = seasonId;
        this.seasonTitle = seasonTitle;
        this.taskCount = taskCount;
        this.assetCount = assetCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(Integer episodeNumber) { this.episodeNumber = episodeNumber; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getPublishDate() { return publishDate; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public EpisodeStatus getStatus() { return status; }
    public void setStatus(EpisodeStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Long getSeasonId() { return seasonId; }
    public void setSeasonId(Long seasonId) { this.seasonId = seasonId; }
    public String getSeasonTitle() { return seasonTitle; }
    public void setSeasonTitle(String seasonTitle) { this.seasonTitle = seasonTitle; }
    public int getTaskCount() { return taskCount; }
    public void setTaskCount(int taskCount) { this.taskCount = taskCount; }
    public int getAssetCount() { return assetCount; }
    public void setAssetCount(int assetCount) { this.assetCount = assetCount; }
}
