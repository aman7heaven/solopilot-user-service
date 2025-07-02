package com.solopilot.user.entity.skills;


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
    name = "tbl_expertise_types",
    indexes = {
        @Index(name = "idx_expertise_uuid", columnList = "uuid"),
        @Index(name = "idx_expertise_is_deleted", columnList = "is_deleted"),
        @Index(name = "idx_expertise_title_deleted", columnList = "first_name, is_deleted")
    })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "expertiseTypeCache")
public class ExpertiseType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "uuid", length = 255, nullable = false, unique = true)
    private String uuid;

    @Column(name = "first_name", length = 255, nullable = false)
    private String title;

    @OneToMany(mappedBy = "expertiseType", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Skill> skills = new ArrayList<>();

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_on")
    private OffsetDateTime dtDeletedOn;

    public void setSkills(List<Skill> skills) {
        this.skills = skills;

        for (Skill skill : skills) {
            skill.setExpertiseType(this);
        }
    }

}
