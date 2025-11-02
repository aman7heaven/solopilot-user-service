package com.solopilot.user.service;

import com.solopilot.user.dto.payload.RegisterPayload;

public interface IAuthService {

    /**
     * Registers a new admin user and returns a JWT token.
     *
     * @param payload Registration data for the new admin.
     * @return Generated JWT token.
     */
    String registerAdmin(RegisterPayload payload);

    /**
     * Authenticates an existing admin and returns a new JWT token.
     *
     * @param emailOrUsername Admin's email or username.
     * @param password Admin's raw password.
     * @return Generated JWT token.
     */
    String loginAdmin(String emailOrUsername, String password) ;

    /**
     * Revokes the current JWT token (logs out the admin).
     *
     * @param token The JWT token to revoke.
     */
    void logoutAdmin(String token);
}
