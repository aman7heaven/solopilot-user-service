package com.solopilot.user.interceptor;

import com.autopilot.utils.StringUtils;
import com.solopilot.user.service.IAnalyticsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Arrays;

import static com.autopilot.constants.AppConstants.COOKIE_SESSION_ID;

@Component
public class PortfolioAnalyticsInterceptor implements HandlerInterceptor {

    private final IAnalyticsService analyticsService;

    public PortfolioAnalyticsInterceptor(IAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String sessionId = null;

        if (request.getCookies() != null) {
            sessionId = Arrays.stream(request.getCookies())
                    .filter(c -> COOKIE_SESSION_ID.equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        if (StringUtils.isNullOrEmpty(sessionId)) {
            sessionId = StringUtils.generateUUID();

            ResponseCookie cookie = ResponseCookie.from(COOKIE_SESSION_ID, sessionId)
                    .httpOnly(true)
                    .secure(true)          // 🔥 REQUIRED in prod
                    .sameSite("None")     // 🔥 REQUIRED cross-site
                    .path("/")
                    .maxAge(Duration.ofMinutes(30))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        analyticsService.trackVisit(sessionId, request);
        return true;
    }

}
