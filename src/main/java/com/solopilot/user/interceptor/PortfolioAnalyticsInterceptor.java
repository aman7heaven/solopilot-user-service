package com.solopilot.user.interceptor;

import com.autopilot.utils.StringUtils;
import com.solopilot.user.service.IAnalyticsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

import static com.autopilot.constants.AppConstants.COOKIE_SESSION_ID;

@Component
public class PortfolioAnalyticsInterceptor implements HandlerInterceptor {

    private final IAnalyticsService analyticsService;

    public PortfolioAnalyticsInterceptor(IAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().equals("/api/v1/portfolio/hero-section")) {
            String sessionId = null;

            if (request.getCookies() != null) {
                sessionId = Arrays.stream(request.getCookies())
                        .filter(cookie -> COOKIE_SESSION_ID.equals(cookie.getName()))
                        .findFirst()
                        .map(Cookie::getValue)
                        .orElse(null);
            }

            if (StringUtils.isNullOrEmpty(sessionId)) {
                sessionId = StringUtils.generateUUID();
                Cookie cookie = new Cookie(COOKIE_SESSION_ID, sessionId);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setMaxAge(60 * 30); // 30 minutes
                response.addCookie(cookie);
            }

            analyticsService.trackVisit(sessionId, request);
        }

        return true;
    }

}
