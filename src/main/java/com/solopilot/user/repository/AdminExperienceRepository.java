package com.solopilot.user.repository;

import com.solopilot.user.dto.response.ExperienceResponse;
import com.solopilot.user.entity.experience.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminExperienceRepository extends JpaRepository<Experience, Long> {

    @Query("""
        SELECT e FROM Experience e
        WHERE e.uuid = :uuid AND e.isDeleted = false
    """)
    Optional<Experience> findExperienceByUuid(String uuid);

    @Query("""
        SELECT new com.solopilot.user.dto.response.ExperienceResponse(
            e.uuid,
            e.companyName,
            e.designation,
            e.location,
            e.summary,
            e.dtStartedOn,
            e.dtEndedOn,
            e.isCurrentlyWorking
        )
        FROM Experience e
        WHERE e.isDeleted = false
        ORDER BY e.dtStartedOn DESC
    """)
    List<ExperienceResponse> getAllAdminExperiences();

    @Modifying
    @Query("""
    UPDATE Experience e
    SET e.isDeleted = true, e.dtDeletedOn = :deletedOn
    WHERE e.uuid = :uuid
""")
    void deleteExperienceByUuid(String uuid, java.time.OffsetDateTime deletedOn);

}
