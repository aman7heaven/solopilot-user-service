package com.solopilot.user.service.impl;

import com.solopilot.user.dto.response.UserPortfolioResponse;
import com.solopilot.user.service.IPortfolioService;
import com.solopilot.user.service.ISkillService;
import com.solopilot.user.service.IUserPortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPortfolioServiceImpl implements IUserPortfolioService {

    private final IPortfolioService portfolioService;
    private final ISkillService skillService;

    @Override
    public UserPortfolioResponse getUserPortfolio() {
        UserPortfolioResponse userPortfolioResponse = new UserPortfolioResponse();
        userPortfolioResponse.setHeroSection(portfolioService.getHeroSection());
        userPortfolioResponse.setAboutSection(portfolioService.getAboutSection());
        userPortfolioResponse.setSkillsSectionList(skillService.getAllExpertiseTypes());
        userPortfolioResponse.setExperienceResponseList(portfolioService.getExperienceSection());
        userPortfolioResponse.setProjectResponseList(portfolioService.getAllProjects());

        return userPortfolioResponse;
    }

}
