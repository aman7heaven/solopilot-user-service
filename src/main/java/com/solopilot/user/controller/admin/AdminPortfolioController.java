package com.solopilot.user.controller.admin;

import com.solopilot.user.dto.payload.*;
import com.solopilot.user.dto.response.*;
import com.solopilot.user.service.IPortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/portfolio")
public class AdminPortfolioController {

    private final IPortfolioService portfolioService;

    @Operation(
            summary = "Update Hero Section",
            description = "Updates the hero section of the portfolio.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Hero section payload",
                    content = @Content(
                            schema = @Schema(implementation = HeroSectionPayload.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "firstName": "John",
                                          "lastName": "Doe",
                                          "professionalTitle": "Software Engineer"
                                        }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Hero section updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/hero-section")
    public ResponseEntity<?> updateHeroSection(@RequestBody HeroSectionPayload payload) {
        portfolioService.updateHeroSection(payload);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Hero Section",
            description = "Retrieves the hero section of the portfolio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hero section retrieved successfully",
                    content = @Content(schema = @Schema(implementation = HeroSectionResponse.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "firstName": "John",
                                          "lastName": "Doe",
                                          "professionalTitle": "Software Engineer"
                                        }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Hero section not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/hero-section")
    public ResponseEntity<?> getHeroSection() {
        HeroSectionResponse resp = portfolioService.getHeroSection();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Operation(
            summary = "Update About Section",
            description = "Updates the about section of the portfolio, including an image upload.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "About section payload and profile image file (multipart/form-data)",
                    content = @Content(
                            schema = @Schema(implementation = AboutSectionPayload.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "aboutMe": "I am a passionate developer.",
                                          "linkedinUrl": "https://linkedin.com/in/johndoe",
                                          "twitterUrl": "https://twitter.com/johndoe",
                                          "GithubUrl": "https://github.com/johndoe"
                                        }
                                    """))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "About section updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or file upload error", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping(value = "/about-section", consumes = {"multipart/form-data"})
    public ResponseEntity<AboutSectionResponse> updateAboutSection(
            @RequestPart AboutSectionPayload payload,
            @RequestPart MultipartFile image) {
        AboutSectionResponse response = portfolioService.updateAboutSection(payload, image);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get About Section",
            description = "Retrieves the about section of the portfolio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "About section retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AboutSectionResponse.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "aboutMe": "I am a passionate developer.",
                                          "profileImageUrl": "https://s3.amazonaws.com/bucket/profile.jpg",
                                          "linkedInUrl": "https://linkedin.com/in/johndoe",
                                          "twitterUrl": "https://twitter.com/johndoe",
                                          "githubUrl": "https://github.com/johndoe"
                                        }
                                    """))),
            @ApiResponse(responseCode = "404", description = "About section not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/about-section")
    public ResponseEntity<?> getAboutSection() {
        AboutSectionResponse resp = portfolioService.getAboutSection();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Operation(
            summary = "Get Experience Section",
            description = "Retrieves all experience entries in the portfolio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Experience section retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ExperienceResponse.class),
                            examples = @ExampleObject(value = """
                                        [
                                          {
                                            "uuid": "e1f2g3h4-i5j6-7890-abcd-ef1234567890",
                                            "companyName": "Acme Corp",
                                            "designation": "Backend Developer",
                                            "location": "Remote",
                                            "summary": "Worked on microservices.",
                                            "dtStartedOn": "2022-01-01T00:00:00Z",
                                            "dtEndedOn": null,
                                            "isCurrentlyWorking": true
                                          }
                                        ]
                                    """))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/experience-section")
    public ResponseEntity<List<ExperienceResponse>> getExperienceSection() {
        List<ExperienceResponse> resp = portfolioService.getExperienceSection();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Operation(
            summary = "Create Experience",
            description = "Creates a new experience entry in the portfolio.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Experience payload",
                    content = @Content(
                            schema = @Schema(implementation = ExperiencePayload.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "companyName": "Acme Corp",
                                          "designation": "Backend Developer",
                                          "location": "Remote",
                                          "summary": "Worked on microservices.",
                                          "dtStartedOn": "2022-01-01T00:00:00Z",
                                          "dtEndedOn": null,
                                          "isCurrentlyWorking": true
                                        }
                                    """))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Experience created successfully",
                    content = @Content(schema = @Schema(implementation = ExperienceResponse.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "uuid": "e1f2g3h4-i5j6-7890-abcd-ef1234567890",
                                          "companyName": "Acme Corp",
                                          "designation": "Backend Developer",
                                          "location": "Remote",
                                          "summary": "Worked on microservices.",
                                          "dtStartedOn": "2022-01-01T00:00:00Z",
                                          "dtEndedOn": null,
                                          "isCurrentlyWorking": true
                                        }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/experience")
    public ResponseEntity<ExperienceResponse> createExperience(@RequestBody ExperiencePayload payload) {
        ExperienceResponse resp = portfolioService.createExperience(payload);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Experience",
            description = "Updates an existing experience entry in the portfolio.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Experience payload",
                    content = @Content(
                            schema = @Schema(implementation = ExperiencePayload.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "companyName": "Acme Corp",
                                          "designation": "Backend Developer",
                                          "location": "Remote",
                                          "summary": "Worked on microservices.",
                                          "dtStartedOn": "2022-01-01T00:00:00Z",
                                          "dtEndedOn": null,
                                          "isCurrentlyWorking": true
                                        }
                                    """))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Experience updated successfully",
                    content = @Content(schema = @Schema(implementation = ExperienceResponse.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "uuid": "e1f2g3h4-i5j6-7890-abcd-ef1234567890",
                                          "companyName": "Acme Corp",
                                          "designation": "Backend Developer",
                                          "location": "Remote",
                                          "summary": "Worked on microservices.",
                                          "dtStartedOn": "2022-01-01T00:00:00Z",
                                          "dtEndedOn": null,
                                          "isCurrentlyWorking": true
                                        }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid input or invalid experience dates",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                        {
                                          "code": 20,
                                          "status": "BAD_REQUEST",
                                          "message": "Please provide valid experience dates: if you’re currently working here, remove the end date; if you’re not, please add one."
                                        }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Experience not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/experience/{experienceId}")
    public ResponseEntity<ExperienceResponse> updatedExperience(
            @Parameter(description = "Experience ID", required = true) @PathVariable String experienceId,
            @RequestBody ExperiencePayload payload) {
        ExperienceResponse resp = portfolioService.updateExperience(experienceId, payload);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Experience",
            description = "Deletes an experience entry from the portfolio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Experience deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Experience not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/experience/{experienceId}")
    public ResponseEntity<?> deleteExperience(
            @Parameter(description = "Experience ID", required = true) @PathVariable String experienceId) {
        portfolioService.deleteExperience(experienceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Get All Projects",
            description = "Retrieves all project entries in the portfolio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ProjectResponse.class),
                            examples = @ExampleObject(value = """
                                        [
                                          {
                                            "uuid": "a4b5c6d7-e8f9-4012-a345-b67890cdef12",
                                            "projectName": "Portfolio Website",
                                            "summary": "A personal portfolio site.",
                                            "gitHubUrl": "https://github.com/johndoe/portfolio",
                                            "deploymentLink": "https://portfolio.johndoe.com"
                                          }
                                        ]
                                    """))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/project-section")
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<ProjectResponse> resp = portfolioService.getAllProjects();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Operation(
            summary = "Create Project",
            description = "Creates a new project entry in the portfolio.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Project payload",
                    content = @Content(
                            schema = @Schema(implementation = ProjectPayload.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "projectName": "Portfolio Website",
                                          "summary": "A personal portfolio site.",
                                          "gitHubUrl": "https://github.com/johndoe/portfolio",
                                          "deploymentLink": "https://portfolio.johndoe.com"
                                        }
                                    """))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project created successfully",
                    content = @Content(schema = @Schema(implementation = ProjectResponse.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "uuid": "a4b5c6d7-e8f9-4012-a345-b67890cdef12",
                                          "projectName": "Portfolio Website",
                                          "summary": "A personal portfolio site.",
                                          "gitHubUrl": "https://github.com/johndoe/portfolio",
                                          "deploymentLink": "https://portfolio.johndoe.com"
                                        }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/project")
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectPayload payload) {
        ProjectResponse resp = portfolioService.createProject(payload);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Project",
            description = "Updates an existing project entry in the portfolio.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Project payload",
                    content = @Content(
                            schema = @Schema(implementation = ProjectPayload.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "projectName": "Portfolio Website",
                                          "summary": "A personal portfolio site.",
                                          "gitHubUrl": "https://github.com/johndoe/portfolio",
                                          "deploymentLink": "https://portfolio.johndoe.com"
                                        }
                                    """))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project updated successfully",
                    content = @Content(schema = @Schema(implementation = ProjectResponse.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "uuid": "a4b5c6d7-e8f9-4012-a345-b67890cdef12",
                                          "projectName": "Portfolio Website",
                                          "summary": "A personal portfolio site.",
                                          "gitHubUrl": "https://github.com/johndoe/portfolio",
                                          "deploymentLink": "https://portfolio.johndoe.com"
                                        }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/project/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(
            @Parameter(description = "Project ID", required = true) @PathVariable String projectId,
            @RequestBody ProjectPayload payload) {
        ProjectResponse resp = portfolioService.updateProject(projectId, payload);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Project",
            description = "Deletes a project entry from the portfolio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<?> deleteProject(
            @Parameter(description = "Project ID", required = true) @PathVariable String projectId) {
        portfolioService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}