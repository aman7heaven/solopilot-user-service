package com.solopilot.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeroSectionResponse {
    private String firstName;
    private String lastName;
    private String professionalTitle;
}
