package com.solopilot.user.controller.admin;

import com.autopilot.config.exception.ApplicationException;
import com.solopilot.user.dto.response.AnalyticsResponse;
import com.solopilot.user.service.IAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final IAnalyticsService analyticsService;

    @GetMapping("/summary")
    @Operation(
            summary = "Get Analytics Summary",
            description = "Fetches the summarized analytics data including visitors, devices, and returning vs new visitors. " +
                    "This endpoint is intended to provide high-level statistics for the UI dashboard.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Analytics summary fetched successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AnalyticsResponse.class),
                                    examples = {
                                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                    name = "Sample Analytics Response",
                                                    summary = "Example of analytics data returned by this API",
                                                    value = """
                                                            {
                                                              "visitors": {
                                                                "today": 123,
                                                                "thisWeek": 890,
                                                                "thisMonth": 4521,
                                                                "thisYear": 52109
                                                              },
                                                              "devices": {
                                                                "desktop": 3421,
                                                                "mobile": 1987,
                                                                "tablet": 113
                                                              },
                                                              "returningVisitors": 2100,
                                                              "newVisitors": 3421
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Server error occurred while fetching analytics data",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApplicationException.class))
                    )
            }
    )
    public ResponseEntity<AnalyticsResponse> getAnalyticsSummary() {
        return new ResponseEntity<>(analyticsService.getAnalyticsSummary(), HttpStatus.OK);
    }
}
