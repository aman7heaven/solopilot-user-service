package com.solopilot.user.dto.payload;

import lombok.Data;

@Data
public class ProjectPayload {
    private String projectName;
    private String summary;
    private String gitHubUrl;
    private String deploymentLink;
}
