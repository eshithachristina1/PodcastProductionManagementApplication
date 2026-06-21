package com.podcastmanagement.repository;

import com.podcastmanagement.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    List<Season> findByPodcastId(Long podcastId);
    List<Season> findByPodcast_TeamId(Long teamId);
}
