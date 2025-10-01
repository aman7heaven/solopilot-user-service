package com.solopilot.user.service;

import com.solopilot.user.dto.response.AnalyticsResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IAnalyticsService {
    void trackVisit(String sessionId, HttpServletRequest request);

    AnalyticsResponse getAnalyticsSummary();
}
