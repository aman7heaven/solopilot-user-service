package com.solopilot.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AboutSectionResponse {
    private String aboutMe;
    private String profileImageUrl;
    private String linkedInUrl;
    private String githubUrl;
    private String twitterUrl;
}
