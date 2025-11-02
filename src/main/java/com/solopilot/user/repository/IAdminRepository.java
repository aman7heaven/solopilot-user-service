package com.solopilot.user.repository;

import com.solopilot.user.dto.response.AboutSectionResponse;
import com.solopilot.user.dto.response.HeroSectionResponse;
import com.solopilot.user.entity.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAdminRepository extends JpaRepository<Admin, Long> {

    Admin findFirstByOrderByIdAsc();

    @Query("SELECT new com.solopilot.user.dto.response.HeroSectionResponse(a.firstName, a.lastName, a.professionalTitle) " +
            "FROM Admin a")
    HeroSectionResponse getHeroSection();

    @Query("SELECT new com.solopilot.user.dto.response.AboutSectionResponse(a.summary, a.imageUrl, a.linkedinUrl, a.githubUrl, a.twitterUrl) " +
            "FROM Admin a")
    AboutSectionResponse getAboutSection();

    Optional<Admin> findByEmailOrUsername(String email, String username);

}
