package com.podcastmanagement.repository;

import com.podcastmanagement.entity.RecordingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordingSessionRepository extends JpaRepository<RecordingSession, Long> {
    List<RecordingSession> findByEpisodeId(Long episodeId);
    List<RecordingSession> findByRecordingDate(LocalDate recordingDate);
    List<RecordingSession> findByRecordingDateAfter(LocalDate date);
    List<RecordingSession> findByEpisode_Season_Podcast_TeamId(Long teamId);
}
