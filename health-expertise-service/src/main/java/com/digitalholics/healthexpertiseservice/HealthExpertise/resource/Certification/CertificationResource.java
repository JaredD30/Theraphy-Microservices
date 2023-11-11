package com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification;

import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.External.PhysiotherapistResource;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class CertificationResource {
    private Integer id;
    private PhysiotherapistResource physiotherapist;
    private String title;
    private String school;
    private Integer year;

}
