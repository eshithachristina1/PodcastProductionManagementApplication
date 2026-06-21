package com.podcastmanagement.service;

import com.podcastmanagement.dto.request.TaskRequest;
import com.podcastmanagement.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(TaskRequest request);

    TaskResponse updateTask(Long id, TaskRequest request);

    void deleteTask(Long id);

    TaskResponse getTaskById(Long id);

    List<TaskResponse> getTasksByEpisodeId(Long episodeId);

    List<TaskResponse> getTasksByUserId(Long userId);

    TaskResponse assignTask(Long taskId, Long userId);

    List<TaskResponse> getAllTasks(Long userId);
}
