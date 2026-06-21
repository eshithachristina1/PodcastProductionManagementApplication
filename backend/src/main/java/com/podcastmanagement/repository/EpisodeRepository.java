package com.podcastmanagement.repository;

import com.podcastmanagement.entity.Episode;
import com.podcastmanagement.enums.EpisodeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findBySeasonId(Long seasonId);
    List<Episode> findByStatus(EpisodeStatus status);
    long countByStatus(EpisodeStatus status);
    List<Episode> findBySeason_Podcast_TeamId(Long teamId);
}
