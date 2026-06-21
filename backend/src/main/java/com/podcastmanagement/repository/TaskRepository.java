package com.podcastmanagement.repository;

import com.podcastmanagement.entity.Task;
import com.podcastmanagement.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByEpisodeId(Long episodeId);
    List<Task> findByAssignedToId(Long userId);
    List<Task> findByStatus(TaskStatus status);
    long countByStatus(TaskStatus status);
    long countByAssignedToId(Long userId);
    List<Task> findByEpisode_Season_Podcast_TeamId(Long teamId);
}
