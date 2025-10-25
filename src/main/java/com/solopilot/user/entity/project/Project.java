package com.solopilot.user.entity.project;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "tbl_project_master")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "uuid", length = 255, nullable = false, unique = true)
    private String uuid;

    @Column(name = "project_name", length = 255, nullable = false)
    private String projectName;

    @Column(name = "summary", length = 1000)
    private String summary;

    @Column(name = "github_url")
    private String gitHubUrl;

    @Column(name = "deployment_link")
    private String deploymentLink;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_on")
    private OffsetDateTime dtDeletedOn;
}
