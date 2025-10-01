package com.solopilot.user.config;

import com.solopilot.user.interceptor.PortfolioAnalyticsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final PortfolioAnalyticsInterceptor analyticsInterceptor;

    public WebConfig(PortfolioAnalyticsInterceptor analyticsInterceptor) {
        this.analyticsInterceptor = analyticsInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(analyticsInterceptor)
                .addPathPatterns("/api/v1/portfolio/**");
    }
}
