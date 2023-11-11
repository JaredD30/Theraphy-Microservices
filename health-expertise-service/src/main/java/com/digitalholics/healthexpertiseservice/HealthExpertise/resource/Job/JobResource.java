package com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job;

import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.External.PhysiotherapistResource;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class JobResource {
    private Integer id;
    private PhysiotherapistResource physiotherapist;
    private String position;
    private String organization;
}
