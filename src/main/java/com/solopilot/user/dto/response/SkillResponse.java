package com.solopilot.user.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SkillResponse {
    private String uuid;
    private String skillName;
    private List<SkillToolResponse> skillTools;
}
