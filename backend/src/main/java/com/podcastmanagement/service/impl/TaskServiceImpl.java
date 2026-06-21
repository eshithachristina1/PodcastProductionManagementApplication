package com.podcastmanagement.service.impl;

import com.podcastmanagement.dto.request.TaskRequest;
import com.podcastmanagement.dto.response.TaskResponse;
import com.podcastmanagement.entity.Episode;
import com.podcastmanagement.entity.RecordingSession;
import com.podcastmanagement.entity.Task;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.exception.ResourceNotFoundException;
import com.podcastmanagement.repository.EpisodeRepository;
import com.podcastmanagement.repository.RecordingSessionRepository;
import com.podcastmanagement.repository.TaskRepository;
import com.podcastmanagement.repository.UserRepository;
import com.podcastmanagement.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EpisodeRepository episodeRepository;
    private final UserRepository userRepository;
    private final RecordingSessionRepository recordingSessionRepository;

    public TaskServiceImpl(TaskRepository taskRepository, EpisodeRepository episodeRepository,
                           UserRepository userRepository, RecordingSessionRepository recordingSessionRepository) {
        this.taskRepository = taskRepository;
        this.episodeRepository = episodeRepository;
        this.userRepository = userRepository;
        this.recordingSessionRepository = recordingSessionRepository;
    }

    @Override
    public TaskResponse createTask(TaskRequest request) {
        Episode episode = episodeRepository.findById(request.getEpisodeId())
                .orElseThrow(() -> new ResourceNotFoundException("Episode", request.getEpisodeId()));
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());
        task.setStatus(request.getStatus() != null ? request.getStatus() : com.podcastmanagement.enums.TaskStatus.PENDING);
        task.setPriority(request.getPriority());
        task.setEpisode(episode);
        if (request.getAssignedToId() != null) {
            User assignedTo = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", request.getAssignedToId()));
            task.setAssignedTo(assignedTo);
        }
        if (request.getCreatedById() != null) {
            User createdByUser = userRepository.findById(request.getCreatedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User", request.getCreatedById()));
            task.setCreatedBy(createdByUser);
        }
        Task savedTask = taskRepository.save(task);
        if (request.getTitle() != null && request.getTitle().toLowerCase().contains("recording")) {
            RecordingSession session = new RecordingSession();
            session.setRecordingDate(LocalDate.now());
            session.setNotes("Auto-created from task: " + savedTask.getTitle());
            session.setEpisode(episode);
            recordingSessionRepository.save(session);
        }
        return toTaskResponse(savedTask);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
        Episode episode = episodeRepository.findById(request.getEpisodeId())
                .orElseThrow(() -> new ResourceNotFoundException("Episode", request.getEpisodeId()));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        task.setPriority(request.getPriority());
        task.setEpisode(episode);
        if (request.getAssignedToId() != null) {
            User assignedTo = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", request.getAssignedToId()));
            task.setAssignedTo(assignedTo);
        } else {
            task.setAssignedTo(null);
        }
        Task updatedTask = taskRepository.save(task);
        return toTaskResponse(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
        taskRepository.delete(task);
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
        return toTaskResponse(task);
    }

    @Override
    public List<TaskResponse> getTasksByEpisodeId(Long episodeId) {
        return taskRepository.findByEpisodeId(episodeId).stream()
                .map(this::toTaskResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getTasksByUserId(Long userId) {
        return taskRepository.findByAssignedToId(userId).stream()
                .map(this::toTaskResponse)
                .toList();
    }

    @Override
    public TaskResponse assignTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        task.setAssignedTo(user);
        Task updatedTask = taskRepository.save(task);
        return toTaskResponse(updatedTask);
    }

    @Override
    public List<TaskResponse> getAllTasks(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (user.getTeam() != null) {
            return taskRepository.findByEpisode_Season_Podcast_TeamId(user.getTeam().getId()).stream()
                    .map(this::toTaskResponse)
                    .toList();
        }
        return taskRepository.findAll().stream()
                .map(this::toTaskResponse)
                .toList();
    }

    private TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDeadline(),
                task.getStatus(),
                task.getPriority(),
                task.getAssignedTo() != null ? task.getAssignedTo().getId() : null,
                task.getAssignedTo() != null ? task.getAssignedTo().getName() : null,
                task.getEpisode().getId(),
                task.getEpisode().getTitle(),
                task.getCreatedBy() != null ? task.getCreatedBy().getId() : null,
                task.getCreatedBy() != null ? task.getCreatedBy().getName() : null
        );
    }
}
