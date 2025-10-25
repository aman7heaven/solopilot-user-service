package com.solopilot.user.controller.admin;

import com.solopilot.user.dto.payload.*;
import com.solopilot.user.dto.response.*;
import com.solopilot.user.service.ISkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/skills")
@Tag(
        name = "skill-controller",
        description = "APIs for managing expertise types, skills, and skill tools in the admin panel"
)
public class AdminSkillController {

    private final ISkillService skillService;

    @Operation(
            summary = "Create Expertise Type",
            description = "Creates a new expertise type category (e.g., Frontend, Backend, DevOps).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Payload containing the title of the expertise type",
                    content = @Content(
                            schema = @Schema(implementation = ExpertiseTypePayload.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "title": "Backend Development"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Expertise type created successfully",
                    content = @Content(schema = @Schema(implementation = ExpertiseTypeResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "uuid": "f7f6c1a4-0b6f-4c63-9b57-993c3e88c2f9",
                                      "title": "Technical"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/expertise")
    public ResponseEntity<ExpertiseTypeResponse> createExpertise(@RequestBody ExpertiseTypePayload payload) {
        ExpertiseTypeResponse resp = skillService.createExpertiseType(payload);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Expertise Type",
            description = "Updates the title of an existing expertise type using its UUID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Updated expertise type payload",
                    content = @Content(
                            schema = @Schema(implementation = ExpertiseTypePayload.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "title": "Soft Skill"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Expertise type updated successfully"),
            @ApiResponse(responseCode = "404", description = "Expertise type not found", content = @Content)
    })
    @PutMapping("/expertise/{expertiseUuid}")
    public ResponseEntity<Void> updateExpertise(
            @Parameter(description = "UUID of the expertise type", example = "f7f6c1a4-0b6f-4c63-9b57-993c3e88c2f9")
            @PathVariable String expertiseUuid,
            @RequestBody ExpertiseTypePayload payload) {
        skillService.updateExpertiseType(expertiseUuid, payload);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get All Expertise Types",
            description = "Fetches all expertise types along with their associated skills and tools."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of expertise types fetched successfully",
                    content = @Content(schema = @Schema(implementation = ExpertiseResponse.class),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "uuid": "f40ee95c-b064-40dd-8e74-1f8714d959d4",
                                            "expertiseName": "Technical",
                                            "skills": [
                                                {
                                                    "uuid": "9f2d7f39-1d15-4281-9ff6-1d7734902398",
                                                    "skillName": "Backend",
                                                    "skillTools": [
                                                        {
                                                            "uuid": "d27f5eb3-2ed2-4451-8f5d-a9fa8dfb2411",
                                                            "name": "SpringBoot"
                                                        },
                                                        {
                                                            "uuid": "e81794cc-2b4f-4997-a218-cbd5e2030b63",
                                                            "name": "Java"
                                                        }
                                                    ]
                                                },
                                                {
                                                    "uuid": "84a78bff-6963-4e1b-a73b-9fe54987572f",
                                                    "skillName": "Frontend",
                                                    "skillTools": []
                                                },
                                                {
                                                    "uuid": "49865824-e128-4a94-a3f0-bc477d28c82d",
                                                    "skillName": "DevOps",
                                                    "skillTools": []
                                                }
                                            ]
                                        }
                                        ]
                                    """)))
    })
    @GetMapping("/expertise/all")
    public ResponseEntity<List<ExpertiseResponse>> getAllExpertises() {
        List<ExpertiseResponse> resp = skillService.getAllExpertiseTypes();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Expertise Type",
            description = "Deletes an expertise type by UUID. All related skills and tools will also be removed."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expertise type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Expertise type not found", content = @Content)
    })
    @DeleteMapping("/expertise/{expertiseUuid}")
    public ResponseEntity<Void> deleteExpertise(
            @Parameter(description = "UUID of the expertise type to delete", example = "f7f6c1a4-0b6f-4c63-9b57-993c3e88c2f9")
            @PathVariable String expertiseUuid) {
        skillService.deleteExpertiseType(expertiseUuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Create Skill under Expertise Type",
            description = "Creates a new skill (e.g., Backend, Frontend, DevOps) under a given expertise type.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Skill payload containing the skill name",
                    content = @Content(
                            schema = @Schema(implementation = SkillPayload.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "Backend"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Skill created successfully",
                    content = @Content(schema = @Schema(implementation = CreateUpdateSkillResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "uuid": "a4b5c6d7-e8f9-4012-a345-b67890cdef12",
                                      "name": "Spring Boot"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Expertise type not found", content = @Content)
    })
    @PostMapping("/expertise/{expertiseUuid}/skills")
    public ResponseEntity<CreateUpdateSkillResponse> createSkill(
            @Parameter(description = "UUID of expertise type", example = "f7f6c1a4-0b6f-4c63-9b57-993c3e88c2f9")
            @PathVariable String expertiseUuid,
            @RequestBody SkillPayload payload) {
        CreateUpdateSkillResponse resp = skillService.createSkill(expertiseUuid, payload);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Skill",
            description = "Updates the name of an existing skill by its UUID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Updated skill payload",
                    content = @Content(
                            schema = @Schema(implementation = SkillPayload.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "Full Stack Development"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Skill updated successfully"),
            @ApiResponse(responseCode = "404", description = "Skill not found", content = @Content)
    })
    @PutMapping("/{skillUuid}")
    public ResponseEntity<Void> updateSkill(
            @Parameter(description = "UUID of skill to update", example = "a4b5c6d7-e8f9-4012-a345-b67890cdef12")
            @PathVariable String skillUuid,
            @RequestBody SkillPayload payload) {
        skillService.updateSkill(skillUuid, payload);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete Skill",
            description = "Deletes a skill by its UUID. All associated skill tools will also be deleted."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Skill deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Skill not found", content = @Content)
    })
    @DeleteMapping("/{skillUuid}")
    public ResponseEntity<Void> deleteSkill(
            @Parameter(description = "UUID of skill to delete", example = "a4b5c6d7-e8f9-4012-a345-b67890cdef12")
            @PathVariable String skillUuid) {
        skillService.deleteSkill(skillUuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Add Tools to a Skill",
            description = "Creates and links multiple tools (e.g., IntelliJ, Postman) to a skill.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Payload containing list of tools",
                    content = @Content(
                            schema = @Schema(implementation = SkillToolPayload.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "skillTools": ["SpringBoot", "Docker", "Postman"]
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Skill tools created successfully",
                    content = @Content(schema = @Schema(implementation = SkillToolResponse.class),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "uuid": "5f91c6e1-9e1c-4a25-b9ea-ec2e97d3c741",
                                        "name": "Postman"
                                      },
                                      {
                                        "uuid": "3d81a4b3-1234-4c89-a12e-5a1f1a93cde5",
                                        "name": "IntelliJ IDEA"
                                      }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "404", description = "Skill not found", content = @Content)
    })
    @PostMapping("/skills/{skillUuid}/tools")
    public ResponseEntity<List<SkillToolResponse>> createSkillTools(
            @Parameter(description = "UUID of skill", example = "a4b5c6d7-e8f9-4012-a345-b67890cdef12")
            @PathVariable String skillUuid,
            @RequestBody SkillToolPayload payload) {
        List<SkillToolResponse> resp = skillService.createSkillTools(skillUuid, payload);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Skill Tool",
            description = "Updates the name of a specific skill tool by its UUID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Updated skill tool payload",
                    content = @Content(
                            schema = @Schema(implementation = UpdateSkillToolPayload.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "VS Code"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Skill tool updated successfully"),
            @ApiResponse(responseCode = "404", description = "Skill tool not found", content = @Content)
    })
    @PutMapping("/SkillTool/{SkillToolUuid}")
    public ResponseEntity<Void> updateSkillTool(
            @Parameter(description = "UUID of skill tool", example = "5f91c6e1-9e1c-4a25-b9ea-ec2e97d3c741")
            @PathVariable String SkillToolUuid,
            @RequestBody UpdateSkillToolPayload payload) {
        skillService.updateSkillTool(SkillToolUuid, payload);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete Skill Tool",
            description = "Deletes a skill tool using its UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Skill tool deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Skill tool not found", content = @Content)
    })
    @DeleteMapping("/SkillTool/{SkillToolUuid}")
    public ResponseEntity<Void> deleteSkillTool(
            @Parameter(description = "UUID of skill tool", example = "5f91c6e1-9e1c-4a25-b9ea-ec2e97d3c741")
            @PathVariable String SkillToolUuid) {
        skillService.deleteSkillTool(SkillToolUuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}