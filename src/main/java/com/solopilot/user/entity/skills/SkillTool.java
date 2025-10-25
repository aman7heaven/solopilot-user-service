package com.solopilot.user.entity.skills;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(
        name = "tbl_skill_tools",
        indexes = {
                @Index(name = "idx_skill_tools_uuid", columnList = "uuid"),
                @Index(name = "idx_fk_skill_uuid", columnList = "fk_skill_uuid"),
                @Index(name = "idx_skill_tools_name", columnList = "skill_name")
        }
)
public class SkillTool {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "uuid", length = 255, nullable = false, unique = true)
    private String uuid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_skill_uuid", referencedColumnName = "uuid")
    @JsonBackReference
    private Skill skill;

    @Column(name = "skill_name", length = 255, nullable = false)
    private String name;

}
