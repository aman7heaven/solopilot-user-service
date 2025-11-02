package com.solopilot.user.dto.payload;

import lombok.Data;

@Data
public class LoginPayload {
    private String emailOrUsername;
    private String password;
}
