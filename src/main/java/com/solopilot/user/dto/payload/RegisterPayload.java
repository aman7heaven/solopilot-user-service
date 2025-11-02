package com.solopilot.user.dto.payload;

import lombok.Data;

@Data
public class RegisterPayload {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
}
