package com.podcastmanagement.service.impl;

import com.podcastmanagement.dto.request.EpisodeRequest;
import com.podcastmanagement.dto.response.EpisodeResponse;
import com.podcastmanagement.entity.Episode;
import com.podcastmanagement.entity.Season;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.enums.EpisodeStatus;
import com.podcastmanagement.exception.ResourceNotFoundException;
import com.podcastmanagement.repository.EpisodeRepository;
import com.podcastmanagement.repository.SeasonRepository;
import com.podcastmanagement.repository.UserRepository;
import com.podcastmanagement.service.EpisodeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpisodeServiceImpl implements EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final SeasonRepository seasonRepository;
    private final UserRepository userRepository;

    public EpisodeServiceImpl(EpisodeRepository episodeRepository, SeasonRepository seasonRepository,
                              UserRepository userRepository) {
        this.episodeRepository = episodeRepository;
        this.seasonRepository = seasonRepository;
        this.userRepository = userRepository;
    }

    @Override
    public EpisodeResponse createEpisode(EpisodeRequest request) {
        Season season = seasonRepository.findById(request.getSeasonId())
                .orElseThrow(() -> new ResourceNotFoundException("Season", request.getSeasonId()));
        Episode episode = new Episode();
        episode.setEpisodeNumber(request.getEpisodeNumber());
        episode.setTitle(request.getTitle());
        episode.setDescription(request.getDescription());
        episode.setPublishDate(request.getPublishDate());
        episode.setDuration(request.getDuration());
        episode.setStatus(request.getStatus() != null ? request.getStatus() : EpisodeStatus.IDEA);
        episode.setSeason(season);
        Episode savedEpisode = episodeRepository.save(episode);
        return toEpisodeResponse(savedEpisode);
    }

    @Override
    public EpisodeResponse updateEpisode(Long id, EpisodeRequest request) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", id));
        Season season = seasonRepository.findById(request.getSeasonId())
                .orElseThrow(() -> new ResourceNotFoundException("Season", request.getSeasonId()));
        episode.setEpisodeNumber(request.getEpisodeNumber());
        episode.setTitle(request.getTitle());
        episode.setDescription(request.getDescription());
        episode.setPublishDate(request.getPublishDate());
        episode.setDuration(request.getDuration());
        if (request.getStatus() != null) {
            episode.setStatus(request.getStatus());
        }
        episode.setSeason(season);
        Episode updatedEpisode = episodeRepository.save(episode);
        return toEpisodeResponse(updatedEpisode);
    }

    @Override
    public void deleteEpisode(Long id) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", id));
        episodeRepository.delete(episode);
    }

    @Override
    public EpisodeResponse getEpisodeById(Long id) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", id));
        return toEpisodeResponse(episode);
    }

    @Override
    public List<EpisodeResponse> getEpisodesBySeasonId(Long seasonId) {
        return episodeRepository.findBySeasonId(seasonId).stream()
                .map(this::toEpisodeResponse)
                .toList();
    }

    @Override
    public EpisodeResponse changeEpisodeStatus(Long id, EpisodeStatus status) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", id));
        episode.setStatus(status);
        Episode updatedEpisode = episodeRepository.save(episode);
        return toEpisodeResponse(updatedEpisode);
    }

    @Override
    public List<EpisodeResponse> getAllEpisodes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (user.getTeam() != null) {
            return episodeRepository.findBySeason_Podcast_TeamId(user.getTeam().getId()).stream()
                    .map(this::toEpisodeResponse)
                    .toList();
        }
        return episodeRepository.findAll().stream()
                .map(this::toEpisodeResponse)
                .toList();
    }

    private EpisodeResponse toEpisodeResponse(Episode episode) {
        return new EpisodeResponse(
                episode.getId(),
                episode.getEpisodeNumber(),
                episode.getTitle(),
                episode.getDescription(),
                episode.getPublishDate(),
                episode.getDuration(),
                episode.getStatus(),
                episode.getCreatedAt(),
                episode.getSeason().getId(),
                episode.getSeason().getTitle(),
                episode.getTasks() != null ? episode.getTasks().size() : 0,
                episode.getAssets() != null ? episode.getAssets().size() : 0
        );
    }
}
