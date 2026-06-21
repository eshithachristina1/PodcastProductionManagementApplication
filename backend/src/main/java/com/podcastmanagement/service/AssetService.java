package com.podcastmanagement.service;

import com.podcastmanagement.dto.response.AssetResponse;
import com.podcastmanagement.enums.AssetType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssetService {

    AssetResponse uploadAsset(Long episodeId, MultipartFile file, AssetType fileType, String name);

    AssetResponse getAssetById(Long id);

    List<AssetResponse> getAssetsByEpisodeId(Long episodeId);

    Resource downloadAsset(Long id);

    void deleteAsset(Long id);

    List<AssetResponse> getAllAssets(Long userId);
}
