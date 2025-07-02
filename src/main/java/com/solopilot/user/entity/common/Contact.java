package com.solopilot.user.entity.common;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_contact_master")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "uuid", length = 255, nullable = false, unique = true)
    private String uuid;

    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "subject", length = 1000)
    private String subject;

    @Column(name = "message")
    private String message;

}
