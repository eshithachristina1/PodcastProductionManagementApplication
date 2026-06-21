package com.podcastmanagement.dto.request;

import com.podcastmanagement.enums.TaskPriority;
import com.podcastmanagement.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class TaskRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private LocalDate deadline;

    private TaskStatus status;

    @NotNull(message = "Priority is required")
    private TaskPriority priority;

    private Long assignedToId;

    @NotNull(message = "Episode ID is required")
    private Long episodeId;

    private Long createdById;

    public TaskRequest() {}

    public TaskRequest(String title, String description, LocalDate deadline, TaskStatus status,
                       TaskPriority priority, Long assignedToId, Long episodeId, Long createdById) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.assignedToId = assignedToId;
        this.episodeId = episodeId;
        this.createdById = createdById;
    }

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
    public Long getEpisodeId() { return episodeId; }
    public void setEpisodeId(Long episodeId) { this.episodeId = episodeId; }
    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long createdById) { this.createdById = createdById; }
}
