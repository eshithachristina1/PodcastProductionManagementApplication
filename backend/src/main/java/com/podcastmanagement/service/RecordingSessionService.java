package com.podcastmanagement.service;

import com.podcastmanagement.dto.request.RecordingSessionRequest;
import com.podcastmanagement.dto.response.RecordingSessionResponse;

import java.util.List;

public interface RecordingSessionService {

    RecordingSessionResponse createRecordingSession(RecordingSessionRequest request);

    RecordingSessionResponse updateRecordingSession(Long id, RecordingSessionRequest request);

    void deleteRecordingSession(Long id);

    RecordingSessionResponse getRecordingSessionById(Long id);

    List<RecordingSessionResponse> getRecordingSessionsByEpisodeId(Long episodeId);

    List<RecordingSessionResponse> getUpcomingRecordings(Long userId);

    List<RecordingSessionResponse> getAllRecordingSessions(Long userId);
}
