package com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMedicalHistoryResource {
    private String gender;
    private Double size;
    private Double weight;
    private String birthplace;
    private String hereditaryHistory;
    private String nonPathologicalHistory;
    private String pathologicalHistory;
}
