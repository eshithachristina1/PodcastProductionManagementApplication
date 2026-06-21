package com.podcastmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;

public class PodcastRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String category;
    private String hostName;

    public PodcastRequest() {}

    public PodcastRequest(String title, String description, String category, String hostName) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.hostName = hostName;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getHostName() { return hostName; }
    public void setHostName(String hostName) { this.hostName = hostName; }
}
