package com.solopilot.user.entity.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.OffsetDateTime;

/**
 * Entity to track visitor analytics for portfolio website.
 * Stores session-based visitor data for analytics and performance insights.
 */
@Data
@Entity
@Table(
        name = "tbl_portfolio_visitor_analytics",
        indexes = {
                @Index(name = "idx_visitor_session_id", columnList = "session_id"),
                @Index(name = "idx_visitor_ip_address", columnList = "ip_address"),
                @Index(name = "idx_visitor_dt_last_visited_timestamp", columnList = "dt_last_visited_timestamp"),
                @Index(name = "idx_visitor_dt_last_visited", columnList = "dt_last_visited"),
                @Index(name = "idx_visitor_country", columnList = "country"),
                @Index(name = "idx_visitor_device_type", columnList = "device_type"),
                @Index(name = "idx_visitor_referrer", columnList = "referrer_source"),
                @Index(name = "idx_composite_date_country", columnList = "dt_last_visited, country"),
                @Index(name = "idx_composite_date_device", columnList = "dt_last_visited, device_type")
        }
)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "portfolioVisitorCache")
public class PortfolioVisitorAnalytics {

    /** Primary key for analytics record */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique identifier tracking one complete portfolio visit session */
    @Column(name = "session_id", length = 255, nullable = false)
    @NotBlank
    private String sessionId;

    @Column(name = "dt_last_visited_timestamp", nullable = false)
    @NotNull
    private OffsetDateTime lastVisitedTimestamp;

    @Column(name = "dt_last_visited", nullable = false)
    @NotNull
    private OffsetDateTime lastVisitedDate;

    /** Visitor's IP address for geolocation and unique visitor identification */
    @Column(name = "ip_address", length = 45, nullable = false)
    @NotBlank
    private String ipAddress;

    /** Complete browser User-Agent string */
    @Column(name = "user_agent", length = 1000, nullable = true)
    private String userAgent;

    /** Country derived from IP geolocation */
    @Column(name = "country", length = 100, nullable = true)
    private String country;

    /** City derived from IP geolocation */
    @Column(name = "city", length = 100, nullable = true)
    private String city;

    /** Region/state derived from IP geolocation */
    @Column(name = "region", length = 100, nullable = true)
    private String region;

    /** Device category: MOBILE, DESKTOP, TABLET */
    @Column(name = "device_type", length = 50, nullable = true)
    private String deviceType;

    /** Browser name: Chrome, Firefox, Safari, etc. */
    @Column(name = "browser_name", length = 100, nullable = true)
    private String browserName;

    /** Browser version number */
    @Column(name = "browser_version", length = 50, nullable = true)
    private String browserVersion;

    /** Operating system: Windows, MacOS, Android, iOS, etc. */
    @Column(name = "operating_system", length = 100, nullable = true)
    private String operatingSystem;

    /** Source that brought visitor to portfolio: Google, LinkedIn, direct, etc. */
    @Column(name = "referrer_source", length = 500, nullable = true)
    private String referrerSource;

    /** Whether this is visitor's first time on the portfolio */
    @Column(name = "is_new_visitor", nullable = false, columnDefinition = "boolean default true")
    private Boolean isNewVisitor = true;

}
