package com.solopilot.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceResponse {
    private String uuid;
    private String companyName;
    private String designation;
    private String location;
    private String summary;
    private OffsetDateTime dtStartedOn;
    private OffsetDateTime dtEndedOn;
    private Boolean isCurrentlyWorking;
}
