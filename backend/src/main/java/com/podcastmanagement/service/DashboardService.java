package com.podcastmanagement.service;

import com.podcastmanagement.dto.response.DashboardResponse;

public interface DashboardService {

    DashboardResponse getDashboardMetrics(Long userId);

}
