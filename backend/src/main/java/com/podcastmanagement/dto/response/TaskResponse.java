package com.podcastmanagement.dto.response;

import com.podcastmanagement.enums.TaskPriority;
import com.podcastmanagement.enums.TaskStatus;
import java.time.LocalDate;

public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private TaskStatus status;
    private TaskPriority priority;
    private Long assignedToId;
    private String assignedToName;
    private Long episodeId;
    private String episodeTitle;
    private Long createdById;
    private String createdByName;

    public TaskResponse() {}

    public TaskResponse(Long id, String title, String description, LocalDate deadline, TaskStatus status,
                        TaskPriority priority, Long assignedToId, String assignedToName,
                        Long episodeId, String episodeTitle, Long createdById, String createdByName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.assignedToId = assignedToId;
        this.assignedToName = assignedToName;
        this.episodeId = episodeId;
        this.episodeTitle = episodeTitle;
        this.createdById = createdById;
        this.createdByName = createdByName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public TaskPriority getPriority() { return priority; }
    public void setPriority(TaskPriority priority) { this.priority = priority; }
    public Long getAssignedToId() { return assignedToId; }
    public void setAssignedToId(Long assignedToId) { this.assignedToId = assignedToId; }
    public String getAssignedToName() { return assignedToName; }
    public void setAssignedToName(String assignedToName) { this.assignedToName = assignedToName; }
    public Long getEpisodeId() { return episodeId; }
    public void setEpisodeId(Long episodeId) { this.episodeId = episodeId; }
    public String getEpisodeTitle() { return episodeTitle; }
    public void setEpisodeTitle(String episodeTitle) { this.episodeTitle = episodeTitle; }
    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long createdById) { this.createdById = createdById; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
}
