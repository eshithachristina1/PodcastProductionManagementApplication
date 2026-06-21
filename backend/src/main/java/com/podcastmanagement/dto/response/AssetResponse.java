package com.podcastmanagement.dto.response;

import com.podcastmanagement.enums.AssetType;
import java.time.LocalDateTime;

public class AssetResponse {

    private Long id;
    private String name;
    private AssetType fileType;
    private String filePath;
    private LocalDateTime uploadDate;
    private Long episodeId;
    private String episodeTitle;

    public AssetResponse() {}

    public AssetResponse(Long id, String name, AssetType fileType, String filePath, LocalDateTime uploadDate,
                         Long episodeId, String episodeTitle) {
        this.id = id;
        this.name = name;
        this.fileType = fileType;
        this.filePath = filePath;
        this.uploadDate = uploadDate;
        this.episodeId = episodeId;
        this.episodeTitle = episodeTitle;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public AssetType getFileType() { return fileType; }
    public void setFileType(AssetType fileType) { this.fileType = fileType; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
    public Long getEpisodeId() { return episodeId; }
    public void setEpisodeId(Long episodeId) { this.episodeId = episodeId; }
    public String getEpisodeTitle() { return episodeTitle; }
    public void setEpisodeTitle(String episodeTitle) { this.episodeTitle = episodeTitle; }
}
