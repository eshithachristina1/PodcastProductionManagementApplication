package com.podcastmanagement.controller;

import com.podcastmanagement.dto.response.ApiResponse;
import com.podcastmanagement.dto.response.DashboardResponse;
import com.podcastmanagement.entity.User;
import com.podcastmanagement.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboardMetrics(@RequestAttribute("currentUser") User currentUser) {
        DashboardResponse response = dashboardService.getDashboardMetrics(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Dashboard metrics retrieved successfully", response));
    }
}