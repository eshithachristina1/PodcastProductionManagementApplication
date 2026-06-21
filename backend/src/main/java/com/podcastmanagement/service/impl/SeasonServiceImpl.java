package com.podcastmanagement.service.impl;

import com.podcastmanagement.dto.request.SeasonRequest;
import com.podcastmanagement.dto.response.SeasonResponse;
import com.podcastmanagement.entity.Podcast;
import com.podcastmanagement.entity.Season;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.exception.ResourceNotFoundException;
import com.podcastmanagement.repository.PodcastRepository;
import com.podcastmanagement.repository.SeasonRepository;
import com.podcastmanagement.repository.UserRepository;
import com.podcastmanagement.service.SeasonService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeasonServiceImpl implements SeasonService {

    private final SeasonRepository seasonRepository;
    private final PodcastRepository podcastRepository;
    private final UserRepository userRepository;

    public SeasonServiceImpl(SeasonRepository seasonRepository, PodcastRepository podcastRepository,
                             UserRepository userRepository) {
        this.seasonRepository = seasonRepository;
        this.podcastRepository = podcastRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SeasonResponse createSeason(SeasonRequest request) {
        Podcast podcast = podcastRepository.findById(request.getPodcastId())
                .orElseThrow(() -> new ResourceNotFoundException("Podcast", request.getPodcastId()));
        Season season = new Season();
        season.setSeasonNumber(request.getSeasonNumber());
        season.setTitle(request.getTitle());
        season.setPodcast(podcast);
        Season savedSeason = seasonRepository.save(season);
        return toSeasonResponse(savedSeason);
    }

    @Override
    public SeasonResponse updateSeason(Long id, SeasonRequest request) {
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season", id));
        Podcast podcast = podcastRepository.findById(request.getPodcastId())
                .orElseThrow(() -> new ResourceNotFoundException("Podcast", request.getPodcastId()));
        season.setSeasonNumber(request.getSeasonNumber());
        season.setTitle(request.getTitle());
        season.setPodcast(podcast);
        Season updatedSeason = seasonRepository.save(season);
        return toSeasonResponse(updatedSeason);
    }

    @Override
    public void deleteSeason(Long id) {
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season", id));
        seasonRepository.delete(season);
    }

    @Override
    public SeasonResponse getSeasonById(Long id) {
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season", id));
        return toSeasonResponse(season);
    }

    @Override
    public List<SeasonResponse> getSeasonsByPodcastId(Long podcastId) {
        return seasonRepository.findByPodcastId(podcastId).stream()
                .map(this::toSeasonResponse)
                .toList();
    }

    @Override
    public List<SeasonResponse> getAllSeasons(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (user.getTeam() != null) {
            return seasonRepository.findByPodcast_TeamId(user.getTeam().getId()).stream()
                    .map(this::toSeasonResponse)
                    .toList();
        }
        return seasonRepository.findAll().stream()
                .map(this::toSeasonResponse)
                .toList();
    }

    private SeasonResponse toSeasonResponse(Season season) {
        return new SeasonResponse(
                season.getId(),
                season.getSeasonNumber(),
                season.getTitle(),
                season.getPodcast().getId(),
                season.getPodcast().getTitle(),
                season.getEpisodes() != null ? season.getEpisodes().size() : 0
        );
    }
}
