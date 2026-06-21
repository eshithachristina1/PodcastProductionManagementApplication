package com.podcastmanagement.service;

import com.podcastmanagement.dto.request.PodcastRequest;
import com.podcastmanagement.dto.response.PodcastResponse;

import java.util.List;

public interface PodcastService {

    PodcastResponse createPodcast(PodcastRequest request, Long userId);

    PodcastResponse updatePodcast(Long id, PodcastRequest request);

    void deletePodcast(Long id);

    PodcastResponse getPodcastById(Long id);

    List<PodcastResponse> getAllPodcasts(Long userId);

}
