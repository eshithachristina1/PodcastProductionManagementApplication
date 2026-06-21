package com.podcastmanagement.service.impl;

import com.podcastmanagement.dto.request.PodcastRequest;
import com.podcastmanagement.dto.response.PodcastResponse;
import com.podcastmanagement.entity.Podcast;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.exception.ResourceNotFoundException;
import com.podcastmanagement.repository.PodcastRepository;
import com.podcastmanagement.repository.UserRepository;
import com.podcastmanagement.service.PodcastService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PodcastServiceImpl implements PodcastService {

    private final PodcastRepository podcastRepository;
    private final UserRepository userRepository;

    public PodcastServiceImpl(PodcastRepository podcastRepository, UserRepository userRepository) {
        this.podcastRepository = podcastRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PodcastResponse createPodcast(PodcastRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Podcast podcast = new Podcast();
        podcast.setTitle(request.getTitle());
        podcast.setDescription(request.getDescription());
        podcast.setCategory(request.getCategory());
        podcast.setHostName(request.getHostName());
        if (user.getTeam() != null) {
            podcast.setTeam(user.getTeam());
        }
        Podcast savedPodcast = podcastRepository.save(podcast);
        return toPodcastResponse(savedPodcast);
    }

    @Override
    public PodcastResponse updatePodcast(Long id, PodcastRequest request) {
        Podcast podcast = podcastRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Podcast", id));
        podcast.setTitle(request.getTitle());
        podcast.setDescription(request.getDescription());
        podcast.setCategory(request.getCategory());
        podcast.setHostName(request.getHostName());
        Podcast updatedPodcast = podcastRepository.save(podcast);
        return toPodcastResponse(updatedPodcast);
    }

    @Override
    public void deletePodcast(Long id) {
        Podcast podcast = podcastRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Podcast", id));
        podcastRepository.delete(podcast);
    }

    @Override
    public PodcastResponse getPodcastById(Long id) {
        Podcast podcast = podcastRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Podcast", id));
        return toPodcastResponse(podcast);
    }

    @Override
    public List<PodcastResponse> getAllPodcasts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (user.getTeam() != null) {
            return podcastRepository.findByTeamId(user.getTeam().getId()).stream()
                    .map(this::toPodcastResponse)
                    .toList();
        }
        return podcastRepository.findAll().stream()
                .map(this::toPodcastResponse)
                .toList();
    }

    private PodcastResponse toPodcastResponse(Podcast podcast) {
        return new PodcastResponse(
                podcast.getId(),
                podcast.getTitle(),
                podcast.getDescription(),
                podcast.getCategory(),
                podcast.getHostName(),
                podcast.getCreatedAt(),
                podcast.getSeasons() != null ? podcast.getSeasons().size() : 0,
                podcast.getTeam() != null ? podcast.getTeam().getId() : null
        );
    }
}
