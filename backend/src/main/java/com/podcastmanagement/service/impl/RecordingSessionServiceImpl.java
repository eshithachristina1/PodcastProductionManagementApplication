package com.podcastmanagement.service.impl;

import com.podcastmanagement.dto.request.RecordingSessionRequest;
import com.podcastmanagement.dto.response.RecordingSessionResponse;
import com.podcastmanagement.entity.Episode;
import com.podcastmanagement.entity.RecordingSession;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.exception.ResourceNotFoundException;
import com.podcastmanagement.repository.EpisodeRepository;
import com.podcastmanagement.repository.RecordingSessionRepository;
import com.podcastmanagement.repository.UserRepository;
import com.podcastmanagement.service.RecordingSessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecordingSessionServiceImpl implements RecordingSessionService {

    private final RecordingSessionRepository recordingSessionRepository;
    private final EpisodeRepository episodeRepository;
    private final UserRepository userRepository;

    public RecordingSessionServiceImpl(RecordingSessionRepository recordingSessionRepository,
                                       EpisodeRepository episodeRepository,
                                       UserRepository userRepository) {
        this.recordingSessionRepository = recordingSessionRepository;
        this.episodeRepository = episodeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RecordingSessionResponse createRecordingSession(RecordingSessionRequest request) {
        Episode episode = episodeRepository.findById(request.getEpisodeId())
                .orElseThrow(() -> new ResourceNotFoundException("Episode", request.getEpisodeId()));
        RecordingSession session = new RecordingSession();
        session.setRecordingDate(request.getRecordingDate());
        session.setRecordingTime(request.getRecordingTime());
        session.setMeetingLink(request.getMeetingLink());
        session.setLocation(request.getLocation());
        session.setNotes(request.getNotes());
        session.setEpisode(episode);
        RecordingSession savedSession = recordingSessionRepository.save(session);
        return toRecordingSessionResponse(savedSession);
    }

    @Override
    public RecordingSessionResponse updateRecordingSession(Long id, RecordingSessionRequest request) {
        RecordingSession session = recordingSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RecordingSession", id));
        Episode episode = episodeRepository.findById(request.getEpisodeId())
                .orElseThrow(() -> new ResourceNotFoundException("Episode", request.getEpisodeId()));
        session.setRecordingDate(request.getRecordingDate());
        session.setRecordingTime(request.getRecordingTime());
        session.setMeetingLink(request.getMeetingLink());
        session.setLocation(request.getLocation());
        session.setNotes(request.getNotes());
        session.setEpisode(episode);
        RecordingSession updatedSession = recordingSessionRepository.save(session);
        return toRecordingSessionResponse(updatedSession);
    }

    @Override
    public void deleteRecordingSession(Long id) {
        RecordingSession session = recordingSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RecordingSession", id));
        recordingSessionRepository.delete(session);
    }

    @Override
    public RecordingSessionResponse getRecordingSessionById(Long id) {
        RecordingSession session = recordingSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RecordingSession", id));
        return toRecordingSessionResponse(session);
    }

    @Override
    public List<RecordingSessionResponse> getRecordingSessionsByEpisodeId(Long episodeId) {
        return recordingSessionRepository.findByEpisodeId(episodeId).stream()
                .map(this::toRecordingSessionResponse)
                .toList();
    }

    @Override
    public List<RecordingSessionResponse> getUpcomingRecordings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (user.getTeam() != null) {
            return recordingSessionRepository
                    .findByEpisode_Season_Podcast_TeamId(user.getTeam().getId()).stream()
                    .filter(s -> s.getRecordingDate() != null && !s.getRecordingDate().isBefore(LocalDate.now()))
                    .map(this::toRecordingSessionResponse)
                    .toList();
        }
        return recordingSessionRepository.findByRecordingDateAfter(LocalDate.now()).stream()
                .map(this::toRecordingSessionResponse)
                .toList();
    }

    @Override
    public List<RecordingSessionResponse> getAllRecordingSessions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (user.getTeam() != null) {
            return recordingSessionRepository
                    .findByEpisode_Season_Podcast_TeamId(user.getTeam().getId()).stream()
                    .map(this::toRecordingSessionResponse)
                    .toList();
        }
        return recordingSessionRepository.findAll().stream()
                .map(this::toRecordingSessionResponse)
                .toList();
    }

    private RecordingSessionResponse toRecordingSessionResponse(RecordingSession session) {
        return new RecordingSessionResponse(
                session.getId(),
                session.getRecordingDate(),
                session.getRecordingTime(),
                session.getMeetingLink(),
                session.getLocation(),
                session.getNotes(),
                session.getEpisode().getId(),
                session.getEpisode().getTitle()
        );
    }
}
