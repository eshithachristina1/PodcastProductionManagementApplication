package com.podcastmanagement.service;

import com.podcastmanagement.dto.request.SeasonRequest;
import com.podcastmanagement.dto.response.SeasonResponse;

import java.util.List;

public interface SeasonService {

    SeasonResponse createSeason(SeasonRequest request);

    SeasonResponse updateSeason(Long id, SeasonRequest request);

    void deleteSeason(Long id);

    SeasonResponse getSeasonById(Long id);

    List<SeasonResponse> getSeasonsByPodcastId(Long podcastId);

    List<SeasonResponse> getAllSeasons(Long userId);
}
