package com.solopilot.user.service.impl;

import com.autopilot.config.logging.AppLogger;
import com.solopilot.user.dto.response.AnalyticsResponse;
import com.solopilot.user.entity.common.PortfolioVisitorAnalytics;
import com.solopilot.user.repository.PortfolioVisitorAnalyticsRepository;
import com.solopilot.user.service.IAnalyticsService;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements IAnalyticsService {

    private final AppLogger log = new AppLogger(LoggerFactory.getLogger(AnalyticsServiceImpl.class));

    private final PortfolioVisitorAnalyticsRepository analyticsRepository;

    @Override
    public void trackVisit(String sessionId, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String referer = request.getHeader("Referer");
        OffsetDateTime now = OffsetDateTime.now();

        // Extract User-Agent header safely
        String userAgentHeader = request.getHeader("User-Agent");
        UserAgent userAgent = (userAgentHeader != null)
                ? UserAgent.parseUserAgentString(userAgentHeader)
                : null;

        Browser browser = (userAgent != null) ? userAgent.getBrowser() : null;
        OperatingSystem os = (userAgent != null) ? userAgent.getOperatingSystem() : null;

        String browserName = (browser != null) ? browser.getName() : "Unknown";
        String browserVersion = (userAgent != null && userAgent.getBrowserVersion() != null)
                ? userAgent.getBrowserVersion().getVersion()
                : "Unknown";
        String operatingSystem = (os != null) ? os.getName() : "Unknown";

        String deviceType = "unknown";
        if (os != null && os.getDeviceType() != null) {
            deviceType = switch (os.getDeviceType()) {
                case COMPUTER -> "desktop";
                case MOBILE -> "mobile";
                case TABLET -> "tablet";
                default -> os.getDeviceType().getName().toLowerCase();
            };
        }

        Optional<PortfolioVisitorAnalytics> existingOpt =
                analyticsRepository.findBySessionId(sessionId);

        if (existingOpt.isPresent()) {
            // Update existing visitor
            PortfolioVisitorAnalytics existing = existingOpt.get();
            existing.setLastVisitedTimestamp(now);
            existing.setLastVisitedDate(now);
            existing.setIpAddress(ipAddress);
            existing.setUserAgent(userAgentHeader);
            existing.setBrowserName(browserName);
            existing.setBrowserVersion(browserVersion);
            existing.setOperatingSystem(operatingSystem);
            existing.setDeviceType(deviceType);
            existing.setReferrerSource(referer);
            existing.setIsNewVisitor(false);

            analyticsRepository.save(existing);
            log.info("Updated analytics for sessionId {}", sessionId);
        } else {
            // Create new record
            PortfolioVisitorAnalytics analytics = new PortfolioVisitorAnalytics();
            analytics.setSessionId(sessionId);
            analytics.setLastVisitedTimestamp(now);
            analytics.setLastVisitedDate(now);
            analytics.setIpAddress(ipAddress);
            analytics.setUserAgent(userAgentHeader);
            analytics.setBrowserName(browserName);
            analytics.setBrowserVersion(browserVersion);
            analytics.setOperatingSystem(operatingSystem);
            analytics.setDeviceType(deviceType);
            analytics.setReferrerSource(referer);
            analytics.setIsNewVisitor(true);

            // Default values for geolocation (until plug in GeoIP API)
            analytics.setCountry(null);
            analytics.setCity(null);
            analytics.setRegion(null);

            analyticsRepository.save(analytics);
            log.info("Created new analytics entry for sessionId {}", sessionId);
        }
    }

    @Override
    public AnalyticsResponse getAnalyticsSummary() {
        OffsetDateTime now = OffsetDateTime.now();

        // Start of today (00:00 local offset)
        OffsetDateTime startOfToday = now.toLocalDate()
                .atStartOfDay()
                .atOffset(now.getOffset());

        // Start of this week (Monday 00:00)
        OffsetDateTime startOfWeek = now.toLocalDate()
                .with(java.time.DayOfWeek.MONDAY)
                .atStartOfDay()
                .atOffset(now.getOffset());

        // Start of this month (1st day 00:00)
        OffsetDateTime startOfMonth = now.toLocalDate()
                .withDayOfMonth(1)
                .atStartOfDay()
                .atOffset(now.getOffset());

        // Start of this year (Jan 1st 00:00)
        OffsetDateTime startOfYear = now.toLocalDate()
                .withDayOfYear(1)
                .atStartOfDay()
                .atOffset(now.getOffset());

        long today = analyticsRepository.countVisitorsSince(startOfToday);
        long thisWeek = analyticsRepository.countVisitorsSince(startOfWeek);
        long thisMonth = analyticsRepository.countVisitorsSince(startOfMonth);
        long thisYear = analyticsRepository.countVisitorsSince(startOfYear);

        long desktop = analyticsRepository.countByDeviceType("desktop");
        long mobile = analyticsRepository.countByDeviceType("mobile");
        long tablet = analyticsRepository.countByDeviceType("tablet");

        long returningVisitors = analyticsRepository.countReturningVisitors();
        long newVisitors = analyticsRepository.countNewVisitors();

        return AnalyticsResponse.builder()
                .visitors(AnalyticsResponse.Visitors.builder()
                        .today(today)
                        .thisWeek(thisWeek)
                        .thisMonth(thisMonth)
                        .thisYear(thisYear)
                        .build())
                .devices(AnalyticsResponse.Devices.builder()
                        .desktop(desktop)
                        .mobile(mobile)
                        .tablet(tablet)
                        .build())
                .returningVisitors(returningVisitors)
                .newVisitors(newVisitors)
                .build();
    }

}
