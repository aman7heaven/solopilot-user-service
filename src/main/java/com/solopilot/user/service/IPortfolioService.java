package com.solopilot.user.service;

import com.solopilot.user.dto.payload.*;
import com.solopilot.user.dto.response.AboutSectionResponse;
import com.solopilot.user.dto.response.ExperienceResponse;
import com.solopilot.user.dto.response.HeroSectionResponse;
import com.solopilot.user.dto.response.ProjectResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPortfolioService {

    void updateHeroSection(HeroSectionPayload payload);

    HeroSectionResponse getHeroSection();

    AboutSectionResponse updateAboutSection(AboutSectionPayload payload, MultipartFile image);

    AboutSectionResponse getAboutSection();

    List<ExperienceResponse> getExperienceSection();

    ExperienceResponse updateExperience(String experienceId, ExperiencePayload payload);

    ExperienceResponse createExperience(ExperiencePayload payload);

    void deleteExperience(String experienceId);

    ProjectResponse createProject(ProjectPayload payload);

    ProjectResponse updateProject(String uuid, ProjectPayload payload);

    void deleteProject(String uuid);

    List<ProjectResponse> getAllProjects();

    void sendMessage(ContactMessagePayload payload);
}
