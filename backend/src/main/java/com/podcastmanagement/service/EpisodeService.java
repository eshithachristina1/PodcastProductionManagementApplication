package com.podcastmanagement.service;

import com.podcastmanagement.dto.request.EpisodeRequest;
import com.podcastmanagement.dto.response.EpisodeResponse;
import com.podcastmanagement.enums.EpisodeStatus;

import java.util.List;

public interface EpisodeService {

    EpisodeResponse createEpisode(EpisodeRequest request);

    EpisodeResponse updateEpisode(Long id, EpisodeRequest request);

    void deleteEpisode(Long id);

    EpisodeResponse getEpisodeById(Long id);

    List<EpisodeResponse> getEpisodesBySeasonId(Long seasonId);

    EpisodeResponse changeEpisodeStatus(Long id, EpisodeStatus status);

    List<EpisodeResponse> getAllEpisodes(Long userId);
}
