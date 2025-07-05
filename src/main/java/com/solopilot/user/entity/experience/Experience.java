package com.solopilot.user.entity.experience;


import com.solopilot.user.entity.skills.Skill;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "tbl_experience_master")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "experienceCache")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "uuid", length = 255, nullable = false, unique = true)
    private String uuid;

    @Column(name = "skill_name", length = 255, nullable = false)
    private String companyName;

    @Column(name = "designation", length = 255, nullable = false)
    private String designation;

    @Column(name = "location", length = 255, nullable = false)
    private String location;

    @Column(name = "summary", length = 500, nullable = false)
    private String summary;

    @Column(name = "started_on")
    private OffsetDateTime dtStartedOn;

    @Column(name = "ended_on")
    private OffsetDateTime dtEndedOn;

    @Column(name="is_currently_working")
    private Boolean isCurrentlyWorking;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_on")
    private OffsetDateTime dtDeletedOn;
}
