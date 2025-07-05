package com.solopilot.user.entity.skills;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(
        name = "tbl_skill_tools",
        indexes = {
                @Index(name = "idx_skill_tools_uuid", columnList = "uuid"),
                @Index(name = "idx_fk_skill_uuid", columnList = "fk_skill_uuid"),
                @Index(name = "idx_skill_tools_is_deleted", columnList = "is_deleted"),
                @Index(name = "idx_skill_tools_name", columnList = "skill_name")
        }
)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "skillToolsCache")
public class SkillTools {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "uuid", length = 255, nullable = false, unique = true)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "fk_skill_uuid", referencedColumnName = "uuid")
    @JsonBackReference
    private Skill skill;

    @Column(name = "skill_name", length = 255, nullable = false)
    private String name;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_on")
    private OffsetDateTime dtDeletedOn;

}
