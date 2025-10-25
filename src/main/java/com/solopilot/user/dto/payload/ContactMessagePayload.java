package com.solopilot.user.dto.payload;

import lombok.Data;

@Data
public class ContactMessagePayload {
    private String name;
    private String email;
    private String subject;
    private String message;
}
