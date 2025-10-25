package com.solopilot.user.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ExpertiseResponse {
    private String uuid;
    private String expertiseName;
    private List<SkillResponse> skills;
}
