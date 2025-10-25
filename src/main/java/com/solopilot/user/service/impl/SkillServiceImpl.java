package com.solopilot.user.service.impl;

import com.autopilot.config.exception.ApplicationException;
import com.autopilot.config.exception.ApplicationExceptionTypes;
import com.autopilot.utils.StringUtils;
import com.solopilot.user.dto.payload.ExpertiseTypePayload;
import com.solopilot.user.dto.payload.SkillPayload;
import com.solopilot.user.dto.payload.SkillToolPayload;
import com.solopilot.user.dto.payload.UpdateSkillToolPayload;
import com.solopilot.user.dto.response.*;
import com.solopilot.user.entity.skills.ExpertiseType;
import com.solopilot.user.entity.skills.Skill;
import com.solopilot.user.entity.skills.SkillTool;
import com.solopilot.user.repository.IExpertiseTypeRepository;
import com.solopilot.user.repository.ISkillRepository;
import com.solopilot.user.repository.ISkillToolRepository;
import com.solopilot.user.service.ISkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements ISkillService {

    private final IExpertiseTypeRepository expertiseRepo;
    private final ISkillRepository skillRepo;
    private final ISkillToolRepository skillToolRepo;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public ExpertiseTypeResponse createExpertiseType(ExpertiseTypePayload payload) {
        ExpertiseType expertiseType = new ExpertiseType();
        expertiseType.setUuid(StringUtils.generateUUID());
        expertiseType.setExpertiseName(payload.getTitle());
        expertiseRepo.save(expertiseType);

        ExpertiseTypeResponse expertiseTypeResponse = new ExpertiseTypeResponse();
        expertiseTypeResponse.setUuid(expertiseType.getUuid());
        expertiseTypeResponse.setTitle(expertiseType.getExpertiseName());
        return expertiseTypeResponse;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void updateExpertiseType(String expertiseUuid, ExpertiseTypePayload payload) {
        ExpertiseType expertiseType = expertiseRepo.findByUuid(expertiseUuid)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.EXPERTISE_DOES_NOT_EXISTS));
        expertiseType.setExpertiseName(payload.getTitle());
        expertiseRepo.save(expertiseType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpertiseResponse> getAllExpertiseTypes() {
        List<ExpertiseType> all = expertiseRepo.findAll();
        return all.stream().map(this::mapToExpertiseResponse).toList();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void deleteExpertiseType(String expertiseUuid) {
        ExpertiseType expertiseType = expertiseRepo.findByUuid(expertiseUuid)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.EXPERTISE_DOES_NOT_EXISTS));

        // Delete all skills and their skillTools
        List<Skill> skills = skillRepo.findAllByExpertiseType_Uuid(expertiseUuid);
        for (Skill skill : skills) {
            // Delete all skill tools for this skill
            skillToolRepo.deleteAllBySkillUuid(skill.getUuid());

            // delete the skill
            skillRepo.deleteByUuid(skill.getUuid());
        }

        // Delete expertise type
        expertiseRepo.deleteByUuid(expertiseType.getUuid());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public CreateUpdateSkillResponse createSkill(String expertiseUuid, SkillPayload payload) {
        ExpertiseType expertiseType = expertiseRepo.findByUuid(expertiseUuid)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.EXPERTISE_DOES_NOT_EXISTS));

        Skill newSkill = new Skill();
        newSkill.setUuid(StringUtils.generateUUID());
        newSkill.setExpertiseType(expertiseType);
        newSkill.setName(payload.getName());

        skillRepo.save(newSkill);

        CreateUpdateSkillResponse skillResponse = new CreateUpdateSkillResponse();
        skillResponse.setName(newSkill.getName());
        skillResponse.setUuid(newSkill.getUuid());

        return skillResponse;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void updateSkill(String skillUuid, SkillPayload payload) {
        Skill skill = skillRepo.findByUuid(skillUuid)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.SKILL_DOES_NOT_EXISTS));
        skill.setName(payload.getName());
        skillRepo.save(skill);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void deleteSkill(String skillUuid) {
        Skill skill = skillRepo.findByUuid(skillUuid)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.SKILL_DOES_NOT_EXISTS));

        // Delete all skill tools for this skill
        skillToolRepo.deleteAllBySkillUuid(skillUuid);

        // delete the skill
        skillRepo.deleteByUuid(skillUuid);
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public List<SkillToolResponse> createSkillTools(String skillUuid, SkillToolPayload payload) {
        Skill skill = skillRepo.findByUuid(skillUuid)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.SKILL_DOES_NOT_EXISTS));

        List<SkillTool> skillToolList = new ArrayList<>();
        List<SkillToolResponse> responseList = new ArrayList<>();
        payload.getSkillTools().forEach((skillToolName) -> {
            SkillTool skillTool = new SkillTool();
            skillTool.setUuid(StringUtils.generateUUID());
            skillTool.setName(skillToolName);
            skillTool.setSkill(skill);
            skillToolList.add(skillTool);

            SkillToolResponse resp = new SkillToolResponse();
            resp.setUuid(skillTool.getUuid());
            resp.setName(skillTool.getName());
            responseList.add(resp);
        });

        skillToolRepo.saveAll(skillToolList);
        return responseList;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void updateSkillTool(String SkillToolUuid, UpdateSkillToolPayload payload) {
        SkillTool skillTool = skillToolRepo.findByUuid(SkillToolUuid)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.SKILL_TOOL_DOES_NOT_EXISTS));
        skillTool.setName(payload.getName());
        skillToolRepo.save(skillTool);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void deleteSkillTool(String skillToolUuid) {
        SkillTool skillTool = skillToolRepo.findByUuid(skillToolUuid)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.SKILL_TOOL_DOES_NOT_EXISTS));
        skillToolRepo.deleteByUuid(skillToolUuid);
    }

    private ExpertiseResponse mapToExpertiseResponse(ExpertiseType expertiseType) {
        ExpertiseResponse resp = new ExpertiseResponse();
        resp.setUuid(expertiseType.getUuid());
        resp.setExpertiseName(expertiseType.getExpertiseName());
        if (expertiseType.getSkills() != null) {
            resp.setSkills(expertiseType.getSkills().stream()
                    .map(this::mapToSkillResponse).toList());
        } else {
            resp.setSkills(Collections.emptyList());
        }
        return resp;
    }

    private SkillResponse mapToSkillResponse(Skill skill) {
        SkillResponse skillResponse = new SkillResponse();
        skillResponse.setUuid(skill.getUuid());
        skillResponse.setSkillName(skill.getName());
        if (skill.getSkillTools() != null) {
            skillResponse.setSkillTools(skill.getSkillTools().stream()
                    .map(this::mapToToolResponse).toList());
        } else {
            skillResponse.setSkillTools(Collections.emptyList());
        }
        return skillResponse;
    }

    private SkillToolResponse mapToToolResponse(SkillTool skillTool) {
        SkillToolResponse skillToolResponse = new SkillToolResponse();
        skillToolResponse.setUuid(skillTool.getUuid());
        skillToolResponse.setName(skillTool.getName());
        return skillToolResponse;
    }
}
