package com.podcastmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SeasonRequest {

    @NotNull(message = "Season number is required")
    private Integer seasonNumber;

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Podcast ID is required")
    private Long podcastId;

    public SeasonRequest() {}

    public SeasonRequest(Integer seasonNumber, String title, Long podcastId) {
        this.seasonNumber = seasonNumber;
        this.title = title;
        this.podcastId = podcastId;
    }

    public Integer getSeasonNumber() { return seasonNumber; }
    public void setSeasonNumber(Integer seasonNumber) { this.seasonNumber = seasonNumber; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Long getPodcastId() { return podcastId; }
    public void setPodcastId(Long podcastId) { this.podcastId = podcastId; }
}
