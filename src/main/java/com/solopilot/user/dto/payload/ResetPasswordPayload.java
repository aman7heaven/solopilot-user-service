package com.solopilot.user.dto.payload;

import lombok.Data;

@Data
public class ResetPasswordPayload {
    private String oldPassword;
    private String newPassword;
}
