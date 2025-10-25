package com.solopilot.user.service;

import com.solopilot.user.dto.payload.ExpertiseTypePayload;
import com.solopilot.user.dto.payload.SkillPayload;
import com.solopilot.user.dto.payload.SkillToolPayload;
import com.solopilot.user.dto.payload.UpdateSkillToolPayload;
import com.solopilot.user.dto.response.ExpertiseResponse;
import com.solopilot.user.dto.response.ExpertiseTypeResponse;
import com.solopilot.user.dto.response.CreateUpdateSkillResponse;
import com.solopilot.user.dto.response.SkillToolResponse;

import java.util.List;

public interface ISkillService {

    ExpertiseTypeResponse createExpertiseType(ExpertiseTypePayload payload);

    void updateExpertiseType(String expertiseUuid, ExpertiseTypePayload payload);

    List<ExpertiseResponse> getAllExpertiseTypes();

    void deleteExpertiseType(String expertiseUuid);

    CreateUpdateSkillResponse createSkill(String expertiseUuid, SkillPayload payload);

    void updateSkill(String skillUuid, SkillPayload payload);

    void deleteSkill(String skillUuid);

    List<SkillToolResponse> createSkillTools(String skillUuid, SkillToolPayload payload);

    void updateSkillTool(String SkillToolUuid, UpdateSkillToolPayload payload);

    void deleteSkillTool(String skillToolUuid);
}
