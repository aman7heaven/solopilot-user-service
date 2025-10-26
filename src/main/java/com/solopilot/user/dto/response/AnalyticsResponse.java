package com.solopilot.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyticsResponse {

    private Visitors visitors;
    private Devices devices;
    private Long totalMessages;

    @Data
    @Builder
    public static class Visitors {
        private long today;
        private long thisWeek;
        private long thisMonth;
        private long thisYear;
    }

    @Data
    @Builder
    public static class Devices {
        private long desktop;
        private long mobile;
        private long tablet;
    }
}
