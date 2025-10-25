package com.solopilot.user.entity.skills;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(
        name = "tbl_skill_types",
        indexes = {
                @Index(name = "idx_skill_uuid", columnList = "uuid"),
                @Index(name = "idx_skill_name", columnList = "skill_name"),
                @Index(name = "idx_fk_expertise_type_uuid", columnList = "fk_expertise_type_uuid")
        }
)
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

    @OneToMany(mappedBy = "skill", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<SkillTool> skillTools = new ArrayList<>();

    @Column(name = "skill_name", length = 255, nullable = false)
    private String name;

    private void setSkillTools(List<SkillTool> skillTools) {
        this.skillTools = skillTools;

        for (SkillTool skillTool : skillTools) {
            skillTool.setSkill(this);
        }
    }

}
