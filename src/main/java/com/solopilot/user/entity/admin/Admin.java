package com.solopilot.user.entity.admin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "tbl_admin_master",
        indexes = {
                @Index(name = "idx_admin_uuid", columnList = "uuid"),
                @Index(name = "idx_admin_username", columnList = "username"),
                @Index(name = "idx_admin_email", columnList = "email"),
                @Index(name = "idx_admin_phone", columnList = "phone_number"),
                @Index(name = "idx_admin_first_name", columnList = "first_name"),
                @Index(name = "idx_admin_last_name", columnList = "last_name")
        }
)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", length = 255, nullable = false, unique = true)
    private String uuid;

    @Column(name = "first_name", length = 255, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 255, nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "professional_title", length = 500, nullable = true)
    private String professionalTitle;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "image_url", length = 1000, nullable = true)
    private String imageUrl;

    @Column(name = "linkedin_url", length = 2000, nullable = true)
    private String linkedinUrl;

    @Column(name = "github_url", length = 2000, nullable = true)
    private String githubUrl;

    @Column(name = "twitter_url", length = 2000, nullable = true)
    private String twitterUrl;
}
