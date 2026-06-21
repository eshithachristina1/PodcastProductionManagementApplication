package com.podcastmanagement.service.impl;

import com.podcastmanagement.dto.response.AssetResponse;
import com.podcastmanagement.entity.Asset;
import com.podcastmanagement.entity.Episode;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.enums.AssetType;
import com.podcastmanagement.exception.ResourceNotFoundException;
import com.podcastmanagement.repository.AssetRepository;
import com.podcastmanagement.repository.EpisodeRepository;
import com.podcastmanagement.repository.UserRepository;
import com.podcastmanagement.service.AssetService;
import com.podcastmanagement.util.FileUploadUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final EpisodeRepository episodeRepository;
    private final FileUploadUtil fileUploadUtil;
    private final UserRepository userRepository;

    public AssetServiceImpl(AssetRepository assetRepository, EpisodeRepository episodeRepository,
                            FileUploadUtil fileUploadUtil, UserRepository userRepository) {
        this.assetRepository = assetRepository;
        this.episodeRepository = episodeRepository;
        this.fileUploadUtil = fileUploadUtil;
        this.userRepository = userRepository;
    }

    @Override
    public AssetResponse uploadAsset(Long episodeId, MultipartFile file, AssetType fileType, String name) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", episodeId));
        String filePath;
        try {
            filePath = fileUploadUtil.uploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
        Asset asset = new Asset();
        asset.setName(name != null ? name : file.getOriginalFilename());
        asset.setFileType(fileType);
        asset.setFilePath(filePath);
        asset.setEpisode(episode);
        Asset savedAsset = assetRepository.save(asset);
        return toAssetResponse(savedAsset);
    }

    @Override
    public AssetResponse getAssetById(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset", id));
        return toAssetResponse(asset);
    }

    @Override
    public List<AssetResponse> getAssetsByEpisodeId(Long episodeId) {
        return assetRepository.findByEpisodeId(episodeId).stream()
                .map(this::toAssetResponse)
                .toList();
    }

    @Override
    public List<AssetResponse> getAllAssets(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (user.getTeam() != null) {
            return assetRepository.findByEpisode_Season_Podcast_TeamId(user.getTeam().getId()).stream()
                    .map(this::toAssetResponse)
                    .toList();
        }
        return assetRepository.findAll().stream()
                .map(this::toAssetResponse)
                .toList();
    }

    @Override
    public Resource downloadAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset", id));
        java.nio.file.Path filePath = fileUploadUtil.getUploadPath().resolve(asset.getFilePath());
        Resource resource = new FileSystemResource(filePath.toFile());
        if (!resource.exists()) {
            throw new ResourceNotFoundException("File not found on disk for asset id: " + id);
        }
        return resource;
    }

    @Override
    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset", id));
        try {
            fileUploadUtil.deleteFile(asset.getFilePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage(), e);
        }
        assetRepository.delete(asset);
    }

    private AssetResponse toAssetResponse(Asset asset) {
        return new AssetResponse(
                asset.getId(),
                asset.getName(),
                asset.getFileType(),
                asset.getFilePath(),
                asset.getUploadDate(),
                asset.getEpisode().getId(),
                asset.getEpisode().getTitle()
        );
    }
}
