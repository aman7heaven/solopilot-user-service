package com.solopilot.user.dto.payload;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AboutSectionPayload {
    private String aboutMe;
    private String linkedinUrl;
    private String twitterUrl;
    private String GithubUrl;
}
