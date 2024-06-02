package com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Physiotherapist;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class CertificationResource {
    private Integer id;
    private Physiotherapist physiotherapist;
    private String title;
    private String school;
    private Integer year;

}
