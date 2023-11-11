package com.digitalholics.healthexpertiseservice.HealthExpertise.resource;

import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.External.PatientResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.External.PhysiotherapistResource;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisResource {

    private Integer id;
    private PhysiotherapistResource physiotherapist;
    private PatientResource patient;
    private String diagnosis;
    private String date;
}
