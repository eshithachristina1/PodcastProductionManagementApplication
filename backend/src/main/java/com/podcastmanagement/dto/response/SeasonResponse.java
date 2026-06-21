package com.podcastmanagement.dto.response;

public class SeasonResponse {

    private Long id;
    private Integer seasonNumber;
    private String title;
    private Long podcastId;
    private String podcastTitle;
    private int episodeCount;

    public SeasonResponse() {}

    public SeasonResponse(Long id, Integer seasonNumber, String title, Long podcastId,
                          String podcastTitle, int episodeCount) {
        this.id = id;
        this.seasonNumber = seasonNumber;
        this.title = title;
        this.podcastId = podcastId;
        this.podcastTitle = podcastTitle;
        this.episodeCount = episodeCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getSeasonNumber() { return seasonNumber; }
    public void setSeasonNumber(Integer seasonNumber) { this.seasonNumber = seasonNumber; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Long getPodcastId() { return podcastId; }
    public void setPodcastId(Long podcastId) { this.podcastId = podcastId; }
    public String getPodcastTitle() { return podcastTitle; }
    public void setPodcastTitle(String podcastTitle) { this.podcastTitle = podcastTitle; }
    public int getEpisodeCount() { return episodeCount; }
    public void setEpisodeCount(int episodeCount) { this.episodeCount = episodeCount; }
}
