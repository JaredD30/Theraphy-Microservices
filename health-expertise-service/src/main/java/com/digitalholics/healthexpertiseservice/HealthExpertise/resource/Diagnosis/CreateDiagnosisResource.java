package com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Diagnosis;

import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class CreateDiagnosisResource {

    private Integer id;
    private Integer patientId;
    private String diagnosis;
    private String date;
}
