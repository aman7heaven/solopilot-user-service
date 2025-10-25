package com.solopilot.user.repository;

import com.solopilot.user.entity.skills.ExpertiseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IExpertiseTypeRepository extends JpaRepository<ExpertiseType, Long> {
    Optional<ExpertiseType> findByUuid(String uuid);

    @Modifying
    @Query("DELETE FROM ExpertiseType e WHERE e.uuid = :uuid")
    void deleteByUuid(@Param("uuid") String uuid);
}
