package com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory;

import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.External.PatientResource;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistoryResource {

    private Integer id;
    private PatientResource patient;
    private String gender;
    private Double size;
    private Double weight;
    private String birthplace;
    private String hereditaryHistory;
    private String nonPathologicalHistory;
    private String pathologicalHistory;
}
