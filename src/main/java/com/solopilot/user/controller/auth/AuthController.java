package com.solopilot.user.controller.auth;

import com.autopilot.config.exception.ApplicationException;
import com.autopilot.config.exception.ApplicationExceptionTypes;
import com.solopilot.user.dto.payload.LoginPayload;
import com.solopilot.user.dto.payload.RegisterPayload;
import com.solopilot.user.dto.payload.ResetPasswordPayload;
import com.solopilot.user.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register Admin",
            description = "Registers a new admin account. Only one admin account can exist in the system.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Admin registration payload",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterPayload.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "firstName": "Aman",
                                      "lastName": "Saxena",
                                      "username": "aman_admin",
                                      "email": "admin@example.com",
                                      "password": "SecurePass123!"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin registered successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
                                      "message": "Admin registered successfully"
                                    }
                                    """))),
            @ApiResponse(responseCode = "409", description = "Admin already exists",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "errorCode": 24,
                                      "status": "CONFLICT",
                                      "message": "An admin account already exists. Multiple admin registrations are not allowed."
                                    }
                                    """))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterPayload payload) {
        String token = authService.registerAdmin(payload);
        return ResponseEntity.ok(Map.of("token", token, "message", "Admin registered successfully"));
    }

    @Operation(
            summary = "Login Admin",
            description = "Authenticates an admin using email/username and password and returns a JWT token.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Login credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginPayload.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "emailOrUsername": "admin@example.com",
                                      "password": "SecurePass123!"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
                                      "message": "Login successful"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "errorCode": 18,
                                      "status": "BAD_REQUEST",
                                      "message": "Invalid Credentials, Please login with valid credentials details"
                                    }
                                    """))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginPayload payload) {
        String token = authService.loginAdmin(payload.getEmailOrUsername(), payload.getPassword());
        return ResponseEntity.ok(Map.of("token", token, "message", "Login successful"));
    }

    @Operation(
            summary = "Logout Admin",
            description = "Logs out the admin by revoking their active JWT token. Provide the token in the Authorization header."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "message": "Logout successful"
                                    }
                                    """))),

            @ApiResponse(responseCode = "400", description = "Invalid credentials",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "errorCode": 12,
                                      "status": "BAD_REQUEST",
                                      "message": "Invalid Credentials, Please login with valid credentials details"
                                    }
                                    """))),

            @ApiResponse(responseCode = "401", description = "Missing token",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "errorCode": 13,
                                      "status": "UNAUTHORIZED",
                                      "message": "Authorization token is missing."
                                    }
                                    """))),

            @ApiResponse(responseCode = "401", description = "Invalid or corrupted token",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "errorCode": 14,
                                      "status": "UNAUTHORIZED",
                                      "message": "Invalid or corrupted token."
                                    }
                                    """))),

            @ApiResponse(responseCode = "401", description = "Expired token",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "errorCode": 15,
                                      "status": "UNAUTHORIZED",
                                      "message": "Your session has expired. Please log in again."
                                    }
                                    """))),

            @ApiResponse(responseCode = "401", description = "Signature verification failed",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "errorCode": 20,
                                      "status": "UNAUTHORIZED",
                                      "message": "Token signature verification failed."
                                    }
                                    """))),

            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "errorCode": 17,
                                      "status": "FORBIDDEN",
                                      "message": "You do not have permission to access this resource."
                                    }
                                    """))),

            @ApiResponse(responseCode = "500", description = "JWT validation failed or internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "errorCode": 19,
                                      "status": "INTERNAL_SERVER_ERROR",
                                      "message": "Unable to validate authentication token."
                                    }
                                    """)))
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logoutAdmin(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", required = true,
                    description = "Authorization header with Bearer token",
                    example = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...")
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ApplicationException(ApplicationExceptionTypes.MISSING_AUTH_TOKEN);
        }

        String token = authHeader.substring(7);
        authService.logoutAdmin(token);
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    @Operation(
            summary = "Reset Admin Password",
            description = "Reset password for the logged-in admin. Provide current password and new password. Authorization header (Bearer token) required.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Reset password payload",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResetPasswordPayload.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "oldPassword": "CurrentPass123!",
                                      "newPassword": "NewSecurePass456!"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset successful",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "message": "Password updated successfully"
                                    }
                                    """))),

            // The 4 specific validation errors (codes 30-33)
            @ApiResponse(responseCode = "400", description = "Invalid payload - missing old/new password",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "code": 30,
                                      "status": "BAD_REQUEST",
                                      "message": "Invalid request. Please provide old and new password.",
                                      "details": null
                                    }
                                    """))),

            @ApiResponse(responseCode = "400", description = "Admin not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "code": 31,
                                      "status": "BAD_REQUEST",
                                      "message": "Admin account not found. Please try again later.",
                                      "details": null
                                    }
                                    """))),

            @ApiResponse(responseCode = "400", description = "Old password incorrect",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "code": 32,
                                      "status": "BAD_REQUEST",
                                      "message": "Old password is incorrect. Please enter the correct current password.",
                                      "details": null
                                    }
                                    """))),

            @ApiResponse(responseCode = "400", description = "New password same as old password",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "code": 33,
                                      "status": "BAD_REQUEST",
                                      "message": "New password cannot be the same as the old password.",
                                      "details": null
                                    }
                                    """))),
            @ApiResponse(responseCode = "401", description = "Missing token",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "code": 13,
                                      "status": "UNAUTHORIZED",
                                      "message": "Authorization token is missing.",
                                      "details": null
                                    }
                                    """))),

            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", required = true,
                    description = "Authorization header with Bearer token",
                    example = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...")
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ResetPasswordPayload payload) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ApplicationException(ApplicationExceptionTypes.MISSING_AUTH_TOKEN);
        }

        String token = authHeader.substring(7);
        authService.resetAdminPassword(token, payload);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }

}
