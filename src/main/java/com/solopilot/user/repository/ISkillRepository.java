package com.solopilot.user.repository;

import com.solopilot.user.entity.skills.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ISkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByUuid(String uuid);

    List<Skill> findAllByExpertiseType_Uuid(String expertiseUuid);

    @Modifying
    @Query("DELETE FROM Skill s WHERE s.uuid = :uuid")
    void deleteByUuid(@Param("uuid") String uuid);
}
