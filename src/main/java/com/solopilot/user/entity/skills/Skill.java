package com.solopilot.user.entity.skills;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(
        name = "tbl_skill_types",
        indexes = {
                @Index(name = "idx_skill_uuid", columnList = "uuid"),
                @Index(name = "idx_skill_name", columnList = "skill_name"),
                @Index(name = "idx_skill_is_deleted", columnList = "is_deleted"),
                @Index(name = "idx_fk_expertise_type_uuid", columnList = "fk_expertise_type_uuid")
        }
)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "skillCache")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    @Column(name = "uuid", length = 255, nullable = false, unique = true)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "fk_expertise_type_uuid", referencedColumnName = "uuid")
    @JsonBackReference
    private ExpertiseType expertiseType;

    @OneToMany(mappedBy = "skill", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<SkillTools> skillTools = new ArrayList<>();

    @Column(name = "skill_name", length = 255, nullable = false)
    private String name;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_on")
    private OffsetDateTime dtDeletedOn;

    private void setSkillTools(List<SkillTools> skillTools) {
        this.skillTools = skillTools;

        for (SkillTools skillTool : skillTools) {
            skillTool.setSkill(this);
        }
    }

}
