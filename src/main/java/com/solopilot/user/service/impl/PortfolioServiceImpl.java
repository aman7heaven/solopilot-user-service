package com.solopilot.user.service.impl;

import com.autopilot.config.exception.ApplicationException;
import com.autopilot.config.exception.ApplicationExceptionTypes;
import com.autopilot.enums.EventType;
import com.autopilot.models.payload.QueuePayload;
import com.autopilot.utils.StringUtils;
import com.portfolio.service.ICloudinaryService;
import com.solopilot.user.dto.payload.*;
import com.solopilot.user.dto.response.AboutSectionResponse;
import com.solopilot.user.dto.response.ExperienceResponse;
import com.solopilot.user.dto.response.HeroSectionResponse;
import com.solopilot.user.dto.response.ProjectResponse;
import com.solopilot.user.entity.admin.Admin;
import com.autopilot.models.payload.Contact;
import com.solopilot.user.entity.experience.Experience;
import com.solopilot.user.entity.project.Project;
import com.solopilot.user.messaging.publisher.UserMessagePublisher;
import com.solopilot.user.repository.AdminContactRepository;
import com.solopilot.user.repository.AdminExperienceRepository;
import com.solopilot.user.repository.AdminProjectRepository;
import com.solopilot.user.repository.IAdminRepository;
import com.solopilot.user.service.IPortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements IPortfolioService {

    private final IAdminRepository adminRepository;
    private final AdminExperienceRepository experienceRepository;
    private final AdminProjectRepository projectRepository;
    private final AdminContactRepository contactRepository;
    private final UserMessagePublisher userMessagePublisher;
    private final ICloudinaryService cloudinaryService;

    @Value("${app.rabbitmq.exchanges.notification}")
    private String notificationExchangeName;

    @Value("${app.rabbitmq.routing-keys.email}")
    private String emailRoutingKey;

    private final static String PROFILE_IMAGE_FOLDER = "portfolio/profileImage";
    private final static String PROFILE_IMAGE_NAME = "profile_image";

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void updateHeroSection(HeroSectionPayload payload) {
        Admin admin = adminRepository.findFirstByOrderByIdAsc();
        admin.setFirstName(payload.getFirstName());
        admin.setLastName(payload.getLastName());
        admin.setProfessionalTitle(payload.getProfessionalTitle());
        adminRepository.save(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public HeroSectionResponse getHeroSection() {
        return adminRepository.getHeroSection();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public AboutSectionResponse updateAboutSection(AboutSectionPayload payload, MultipartFile image) {
        String uploadImageUrl = "";

        try {
            uploadImageUrl = cloudinaryService.uploadFile(image, PROFILE_IMAGE_NAME, PROFILE_IMAGE_FOLDER);
        } catch (ApplicationException e) {
            // Don't throw an exception here, just log the error and continue
        }

        Admin admin = adminRepository.findFirstByOrderByIdAsc();
        admin.setSummary(payload.getAboutMe());
        admin.setLinkedinUrl(payload.getLinkedinUrl());
        admin.setTwitterUrl(payload.getTwitterUrl());
        admin.setGithubUrl(payload.getGithubUrl());
        if (StringUtils.isNotNullOrEmpty(uploadImageUrl)) admin.setImageUrl(uploadImageUrl);
        adminRepository.save(admin);

        AboutSectionResponse response = new AboutSectionResponse();
        response.setAboutMe(admin.getSummary());
        response.setLinkedInUrl(admin.getLinkedinUrl());
        response.setTwitterUrl(admin.getTwitterUrl());
        response.setGithubUrl(admin.getGithubUrl());
        response.setProfileImageUrl(admin.getImageUrl());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public AboutSectionResponse getAboutSection() {
        return adminRepository.getAboutSection();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperienceResponse> getExperienceSection() {
        return experienceRepository.getAllAdminExperiences();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ExperienceResponse updateExperience(String experienceId, ExperiencePayload payload) {
        Experience experience = experienceRepository.findExperienceByUuid(experienceId)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.EXPERIENCE_DOES_NOT_EXISTS));

        experience.setCompanyName(payload.getCompanyName());
        experience.setDesignation(payload.getDesignation());
        experience.setLocation(payload.getLocation());
        experience.setSummary(payload.getSummary());
        experience.setDtStartedOn(payload.getDtStartedOn());
        experience.setDtEndedOn(payload.getDtEndedOn());
        experience.setIsCurrentlyWorking(payload.getIsCurrentlyWorking());

        Experience updatedExp = experienceRepository.save(experience);
        return mapToExperienceResponse(updatedExp);
    }

    @Override
    public ExperienceResponse createExperience(ExperiencePayload payload) {
        Experience experience = new Experience();
        experience.setUuid(StringUtils.generateUUID());
        experience.setCompanyName(payload.getCompanyName());
        experience.setDesignation(payload.getDesignation());
        experience.setLocation(payload.getLocation());
        experience.setSummary(payload.getSummary());
        experience.setDtStartedOn(payload.getDtStartedOn());
        if ((payload.getDtEndedOn() == null && payload.getIsCurrentlyWorking() != null && !payload.getIsCurrentlyWorking()) ||
                (payload.getDtEndedOn() != null && payload.getIsCurrentlyWorking() != null && payload.getIsCurrentlyWorking())) {
            throw new ApplicationException(ApplicationExceptionTypes.INVALID_EXPERIENCE_DATES);
        }
        experience.setDtEndedOn(payload.getDtEndedOn());
        experience.setIsCurrentlyWorking(payload.getIsCurrentlyWorking());
        experience.setIsDeleted(false);

        experienceRepository.save(experience);
        return mapToExperienceResponse(experience);
    }

    @Override
    public void deleteExperience(String experienceId) {
        Experience experience = experienceRepository.findExperienceByUuid(experienceId)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.EXPERIENCE_DOES_NOT_EXISTS));
        experienceRepository.deleteExperienceByUuid(experience.getUuid(), OffsetDateTime.now());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ProjectResponse createProject(ProjectPayload payload) {
        Project project = new Project();
        project.setUuid(StringUtils.generateUUID());
        project.setProjectName(payload.getProjectName());
        project.setSummary(payload.getSummary());
        project.setGitHubUrl(payload.getGitHubUrl());
        project.setDeploymentLink(payload.getDeploymentLink());
        project.setIsDeleted(false);

        projectRepository.save(project);
        return mapToProjectResponse(project);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ProjectResponse updateProject(String uuid, ProjectPayload payload) {
        Project project = projectRepository.findProjectByUuid(uuid)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.PROJECT_DOES_NOT_EXISTS));

        project.setProjectName(payload.getProjectName());
        project.setSummary(payload.getSummary());
        project.setGitHubUrl(payload.getGitHubUrl());
        project.setDeploymentLink(payload.getDeploymentLink());

        projectRepository.save(project);
        return mapToProjectResponse(project);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void deleteProject(String uuid) {
        Project project = projectRepository.findProjectByUuid(uuid)
                .orElseThrow(() -> new ApplicationException(ApplicationExceptionTypes.PROJECT_DOES_NOT_EXISTS));
        projectRepository.deleteProjectByUuid(uuid, OffsetDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.getAllProjects();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void sendMessage(ContactMessagePayload payload) {
        Contact contact = new Contact();

        contact.setUuid(StringUtils.generateUUID());
        contact.setMessage(payload.getMessage());
        contact.setEmail(payload.getEmail());
        contact.setName(payload.getName());
        contact.setSubject(payload.getSubject());
        contact.setDtCreatedOn(OffsetDateTime.now());

        contactRepository.save(contact);
        userMessagePublisher.publishWithRoutingKey(
                notificationExchangeName,
                emailRoutingKey,
                QueuePayload.builder().eventType(EventType.CONTACT_MESSAGE_SENT).payload(contact).eventTime(OffsetDateTime.now()).build(),
                Map.of());
    }

    private ProjectResponse mapToProjectResponse(Project project) {
        return ProjectResponse.builder()
                .uuid(project.getUuid())
                .projectName(project.getProjectName())
                .summary(project.getSummary())
                .gitHubUrl(project.getGitHubUrl())
                .deploymentLink(project.getDeploymentLink())
                .build();
    }

    private ExperienceResponse mapToExperienceResponse(Experience experience) {
        ExperienceResponse response = new ExperienceResponse();
        response.setUuid(experience.getUuid());
        response.setCompanyName(experience.getCompanyName());
        response.setDesignation(experience.getDesignation());
        response.setLocation(experience.getLocation());
        response.setSummary(experience.getSummary());
        response.setDtStartedOn(experience.getDtStartedOn());
        response.setDtEndedOn(experience.getDtEndedOn());
        response.setIsCurrentlyWorking(experience.getIsCurrentlyWorking());
        return response;
    }

}
