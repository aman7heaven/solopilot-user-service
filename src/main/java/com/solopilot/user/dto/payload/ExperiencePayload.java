package com.solopilot.user.dto.payload;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ExperiencePayload {
    private String companyName;
    private String designation;
    private String location;
    private String summary;
    private OffsetDateTime dtStartedOn;
    private OffsetDateTime dtEndedOn;
    private Boolean isCurrentlyWorking;
}
