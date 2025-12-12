package com.solopilot.user.controller.user;

import com.solopilot.user.dto.payload.ContactMessagePayload;
import com.solopilot.user.dto.response.UserPortfolioResponse;
import com.solopilot.user.service.IPortfolioService;
import com.solopilot.user.service.IUserPortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposing the public API to retrieve a user's complete portfolio.
 * <p>
 * This includes all portfolio sections (Hero, About, Skills, Experience, Projects)
 * which are displayed on the public portfolio website.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/portfolio")
public class UserPortfolioController {

    private final IUserPortfolioService userPortfolioService;
    private final IPortfolioService portfolioService;

    /**
     * Retrieves the complete portfolio of the user.
     * <p>
     * The response is publicly accessible and contains all information
     * needed to render the user's portfolio website frontend.
     * </p>
     *
     * @return {@link UserPortfolioResponse} containing all portfolio sections
     */
    @Operation(
            summary = "Get Complete User Portfolio",
            description = """
                    Retrieves the full portfolio data of the user including hero, about, skills, experience, and project sections.
                    <br><br>
                    This endpoint is <b>public</b> and can be consumed by the frontend to dynamically display portfolio information.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Portfolio retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = UserPortfolioResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "heroSection": {
                                        "firstName": "Johnny",
                                        "lastName": "Does",
                                        "professionalTitle": "Full Stack Developer & Tech Enthusiast"
                                      },
                                      "aboutSection": {
                                        "aboutMe": "Passionate software developer specializing in Spring Boot and React with a focus on building clean, scalable applications.",
                                        "profileImageUrl": "https://example.com/images/profile.jpg",
                                        "linkedInUrl": "https://linkedin.com/in/johnnydoes",
                                        "twitterUrl": "https://twitter.com/johnnydoes",
                                        "githubUrl": "https://github.com/johnnydoes"
                                      },
                                      "skillsSectionList": [
                                        {
                                          "uuid": "f40ee95c-b064-40dd-8e74-1f8714d959d4",
                                          "expertiseName": "Technical Expertise",
                                          "skills": [
                                            {
                                              "uuid": "c9b5ded3-38bd-42e5-a554-7a4a7c25e3a1",
                                              "skillName": "Backend Development",
                                              "skillTools": [
                                                {
                                                  "uuid": "9370b4c4-764b-4eac-9ab9-6da7c5d7c694",
                                                  "name": "Spring Boot"
                                                },
                                                {
                                                  "uuid": "ddc8f865-0c0d-4b51-9a93-442741a0a1fc",
                                                  "name": "Java"
                                                },
                                                {
                                                  "uuid": "2e2ad3e4-12cb-441a-9f81-982bcb0a81c4",
                                                  "name": "PostgreSQL"
                                                }
                                              ]
                                            },
                                            {
                                              "uuid": "2b001e92-ca8d-4f25-ac13-735e944b5f96",
                                              "skillName": "Frontend Development",
                                              "skillTools": [
                                                {
                                                  "uuid": "0b36f4ae-48d3-43a9-b7da-bd407dcbd722",
                                                  "name": "React"
                                                },
                                                {
                                                  "uuid": "d19b5c8e-f657-4583-b0b1-c9cbcf72a22c",
                                                  "name": "Material-UI"
                                                }
                                              ]
                                            },
                                            {
                                              "uuid": "5c46346e-8598-4515-988b-04fa121b0010",
                                              "skillName": "DevOps",
                                              "skillTools": [
                                                {
                                                  "uuid": "f9b64a4e-5b5b-4b79-91c8-68a9a43c4e8c",
                                                  "name": "Docker"
                                                },
                                                {
                                                  "uuid": "db18a7e3-d01b-4f36-a48e-bc42a7a7a7e4",
                                                  "name": "GitHub Actions"
                                                }
                                              ]
                                            }
                                          ]
                                        },
                                        {
                                          "uuid": "a13bde94-bf74-4a6d-bf28-3e10a65f94a1",
                                          "expertiseName": "Soft Skills & Collaboration Tools",
                                          "skills": [
                                            {
                                              "uuid": "ea9b78a1-6b39-4eb6-93e9-c51b7a3e30e0",
                                              "skillName": "Communication",
                                              "skillTools": [
                                                {
                                                  "uuid": "ecf22d88-3291-4d6a-9586-13411f1ab84a",
                                                  "name": "Slack"
                                                },
                                                {
                                                  "uuid": "b7c65a23-3c04-43bb-a9a2-94e9dfbc7f8f",
                                                  "name": "Microsoft Teams"
                                                },
                                                {
                                                  "uuid": "e6fa224b-2983-4d04-8f8f-21d9876d9b12",
                                                  "name": "Jira"
                                                }
                                              ]
                                            },
                                            {
                                              "uuid": "a81234d2-44a3-4ff1-9381-fbd687da1b28",
                                              "skillName": "Leadership",
                                              "skillTools": []
                                            },
                                            {
                                              "uuid": "dbf78e91-7234-4ed3-991d-64a8b8c1ab11",
                                              "skillName": "Problem Solving",
                                              "skillTools": []
                                            }
                                          ]
                                        }
                                      ],
                                      "experienceResponseList": [
                                        {
                                          "uuid": "cc2232f8-a7a0-4474-a5cf-a70b1ce5c1b0",
                                          "companyName": "HashedIn",
                                          "designation": "SDE 1",
                                          "location": "Gurugram, Haryana",
                                          "summary": "Worked as a Backend Developer contributing to scalable API design and integration.",
                                          "dtStartedOn": "2025-06-01T03:30:00Z",
                                          "dtEndedOn": null,
                                          "isCurrentlyWorking": true
                                        },
                                        {
                                          "uuid": "6ae7ed1e-0a02-4ffe-9901-9650bf66c904",
                                          "companyName": "Invince",
                                          "designation": "Full Stack Developer",
                                          "location": "Pune, India",
                                          "summary": "Developed REST APIs using Spring Boot and built responsive UIs using React.",
                                          "dtStartedOn": "2023-07-01T03:30:00Z",
                                          "dtEndedOn": "2024-08-15T12:30:00Z",
                                          "isCurrentlyWorking": false
                                        }
                                      ],
                                      "projectResponseList": [
                                        {
                                          "uuid": "6b76c18b-2339-4706-a48c-68b203259749",
                                          "projectName": "MelodyMix Music Player - for GenZ",
                                          "summary": "A full-stack music player web app built with React and Spring Boot, featuring playlist management and dynamic song streaming.",
                                          "gitHubUrl": "https://github.com/amansaxena/melodymixes",
                                          "deploymentLink": "https://melodymix.vercel.app"
                                        }
                                      ]
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "404", description = "Portfolio not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<UserPortfolioResponse> getUserPortfolio() {
        return ResponseEntity.ok(userPortfolioService.getUserPortfolio());
    }

    @Operation(
            summary = "Send a Contact Message",
            description = "Allows users to send a message or inquiry via the portfolio contact form.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Contact message payload",
                    content = @Content(
                            schema = @Schema(implementation = ContactMessagePayload.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "name": "Aman Saxena",
                                          "email": "aman.saxena@example.com",
                                          "subject": "Collaboration Opportunity",
                                          "message": "Hi Aman, I really liked your portfolio! Would you be interested in collaborating on a project?"
                                        }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Message sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestBody ContactMessagePayload payload) {
        portfolioService.sendMessage(payload);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
