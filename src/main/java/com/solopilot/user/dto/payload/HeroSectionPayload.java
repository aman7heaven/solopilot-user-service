package com.solopilot.user.dto.payload;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class HeroSectionPayload {
    private String firstName;
    private String lastName;
    private String professionalTitle;
}
