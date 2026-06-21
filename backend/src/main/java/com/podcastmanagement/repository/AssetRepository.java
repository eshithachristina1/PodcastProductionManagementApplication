package com.podcastmanagement.repository;

import com.podcastmanagement.entity.Asset;
import com.podcastmanagement.enums.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByEpisodeId(Long episodeId);
    List<Asset> findByFileType(AssetType fileType);
    List<Asset> findByEpisode_Season_Podcast_TeamId(Long teamId);
}
