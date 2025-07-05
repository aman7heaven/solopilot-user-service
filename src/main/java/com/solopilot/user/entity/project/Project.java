package com.solopilot.user.entity.project;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@Entity
@Table(name = "tbl_project_master")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "projectCache")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "uuid", length = 255, nullable = false, unique = true)
    private String uuid;

    @Column(name = "summary", length = 1000)
    private String summary;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "github_url")
    private String gitHubUrl;

    @Column(name = "deployment_link")
    private String deploymentLink;
}
