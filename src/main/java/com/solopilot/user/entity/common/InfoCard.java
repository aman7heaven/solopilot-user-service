package com.solopilot.user.entity.common;

import com.solopilot.user.enums.SectionType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "tbl_info_cards")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "infoCardsCache")
public class InfoCard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "uuid", length = 255, nullable = false)
    private String uuid;

    @Column(name = "first_name", length = 255, nullable = false)
    private String title;

    @Column(name = "last_name", length = 500, nullable = false)
    private String subTitle;

    @Column(name = "image_url", length = 1000, nullable = true)
    private String imageUrl;

    @Column(name = "section_type")
    private SectionType sectionType;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_on")
    private OffsetDateTime dtDeletedOn;
}
