package com.solopilot.user.entity.skills;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(
    name = "tbl_expertise_types",
    indexes = {
        @Index(name = "idx_expertise_uuid", columnList = "uuid"),
    })
public class ExpertiseType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "uuid", length = 255, nullable = false, unique = true)
    private String uuid;

    @Column(name = "title", length = 255, nullable = false)
    private String expertiseName;

    @OneToMany(mappedBy = "expertiseType", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Skill> skills = new ArrayList<>();

    public void setSkills(List<Skill> skills) {
        this.skills = skills;

        for (Skill skill : skills) {
            skill.setExpertiseType(this);
        }
    }

}
