package com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Patient;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistoryResource {

    private Integer id;
    private Patient patient;
    private String gender;
    private Double size;
    private Double weight;
    private String birthplace;
    private String hereditaryHistory;
    private String nonPathologicalHistory;
    private String pathologicalHistory;
}
