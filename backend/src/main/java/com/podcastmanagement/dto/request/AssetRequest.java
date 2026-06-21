package com.podcastmanagement.dto.request;

import com.podcastmanagement.enums.AssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AssetRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "File type is required")
    private AssetType fileType;

    @NotNull(message = "Episode ID is required")
    private Long episodeId;

    public AssetRequest() {}

    public AssetRequest(String name, AssetType fileType, Long episodeId) {
        this.name = name;
        this.fileType = fileType;
        this.episodeId = episodeId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public AssetType getFileType() { return fileType; }
    public void setFileType(AssetType fileType) { this.fileType = fileType; }
    public Long getEpisodeId() { return episodeId; }
    public void setEpisodeId(Long episodeId) { this.episodeId = episodeId; }
}
