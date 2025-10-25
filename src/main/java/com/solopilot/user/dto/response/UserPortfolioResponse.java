package com.solopilot.user.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserPortfolioResponse {
    HeroSectionResponse heroSection;
    AboutSectionResponse aboutSection;
    List<ExpertiseResponse> skillsSectionList;
    List<ExperienceResponse> experienceResponseList;
    List<ProjectResponse> projectResponseList;
}
