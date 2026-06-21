package com.podcastmanagement.controller;

import com.podcastmanagement.dto.response.ApiResponse;
import com.podcastmanagement.dto.response.AssetResponse;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.enums.AssetType;
import com.podcastmanagement.service.AssetService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping("/upload/{episodeId}")
    public ResponseEntity<ApiResponse<AssetResponse>> uploadAsset(
            @PathVariable Long episodeId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") AssetType fileType,
            @RequestParam("name") String name,
            @RequestAttribute("currentUser") User currentUser) {
        AssetResponse response = assetService.uploadAsset(episodeId, file, fileType, name);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Asset uploaded successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetResponse>> getAssetById(@PathVariable Long id) {
        AssetResponse response = assetService.getAssetById(id);
        return ResponseEntity.ok(ApiResponse.success("Asset retrieved successfully", response));
    }

    @GetMapping("/episode/{episodeId}")
    public ResponseEntity<ApiResponse<List<AssetResponse>>> getAssetsByEpisodeId(@PathVariable Long episodeId) {
        List<AssetResponse> response = assetService.getAssetsByEpisodeId(episodeId);
        return ResponseEntity.ok(ApiResponse.success("Assets retrieved successfully", response));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadAsset(@PathVariable Long id) {
        Resource resource = assetService.downloadAsset(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAsset(@PathVariable Long id,
                                                           @RequestAttribute("currentUser") User currentUser) {
        assetService.deleteAsset(id);
        return ResponseEntity.ok(ApiResponse.success("Asset deleted successfully", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AssetResponse>>> getAllAssets(@RequestAttribute("currentUser") User currentUser) {
        List<AssetResponse> response = assetService.getAllAssets(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Assets retrieved successfully", response));
    }
}