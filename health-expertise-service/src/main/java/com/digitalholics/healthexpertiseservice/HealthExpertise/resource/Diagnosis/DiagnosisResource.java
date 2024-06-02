package com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Diagnosis;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Patient;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Physiotherapist;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisResource {

    private Integer id;
    private Physiotherapist physiotherapist;
    private Patient patient;
    private String diagnosis;
    private String date;
}
