package com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Physiotherapist;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class JobResource {
    private Integer id;
    private Physiotherapist physiotherapist;
    private String position;
    private String organization;
}
