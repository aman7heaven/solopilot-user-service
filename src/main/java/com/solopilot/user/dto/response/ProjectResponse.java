package com.solopilot.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private String uuid;
    private String projectName;
    private String summary;
    private String gitHubUrl;
    private String deploymentLink;
}
