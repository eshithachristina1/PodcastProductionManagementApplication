package com.podcastmanagement.dto.request;

import com.podcastmanagement.enums.EpisodeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class EpisodeRequest {

    private Integer episodeNumber;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private LocalDate publishDate;
    private Integer duration;

    private EpisodeStatus status;

    @NotNull(message = "Season ID is required")
    private Long seasonId;

    public EpisodeRequest() {}

    public EpisodeRequest(Integer episodeNumber, String title, String description, LocalDate publishDate,
                          Integer duration, EpisodeStatus status, Long seasonId) {
        this.episodeNumber = episodeNumber;
        this.title = title;
        this.description = description;
        this.publishDate = publishDate;
        this.duration = duration;
        this.status = status;
        this.seasonId = seasonId;
    }

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
    public Long getSeasonId() { return seasonId; }
    public void setSeasonId(Long seasonId) { this.seasonId = seasonId; }
}
