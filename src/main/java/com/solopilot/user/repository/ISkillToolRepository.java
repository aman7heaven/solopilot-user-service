package com.solopilot.user.repository;

import com.solopilot.user.entity.skills.SkillTool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ISkillToolRepository extends JpaRepository<SkillTool, Long> {
    Optional<SkillTool> findByUuid(String uuid);

    @Modifying
    @Query("DELETE FROM SkillTool st WHERE st.skill.uuid = :skillUuid")
    void deleteAllBySkillUuid(@Param("skillUuid") String skillUuid);

    @Modifying
    @Query("DELETE FROM SkillTool st WHERE st.uuid = :uuid")
    void deleteByUuid(@Param("uuid") String uuid);
}

