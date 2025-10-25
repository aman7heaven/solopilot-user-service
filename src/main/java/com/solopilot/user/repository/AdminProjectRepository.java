package com.solopilot.user.repository;

import com.solopilot.user.dto.response.ProjectResponse;
import com.solopilot.user.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p WHERE p.uuid = :uuid AND p.isDeleted = false")
    Optional<Project> findProjectByUuid(String uuid);

    @Query("""
                SELECT new com.solopilot.user.dto.response.ProjectResponse(
                    p.uuid,
                    p.projectName,
                    p.summary,
                    p.gitHubUrl,
                    p.deploymentLink
                )
                FROM Project p
                WHERE p.isDeleted = false
                ORDER BY p.projectName ASC
            """)
    List<ProjectResponse> getAllProjects();

    @Modifying
    @Query("UPDATE Project p SET p.isDeleted = true, p.dtDeletedOn = :deletedOn WHERE p.uuid = :uuid")
    void deleteProjectByUuid(String uuid, OffsetDateTime deletedOn);
}
