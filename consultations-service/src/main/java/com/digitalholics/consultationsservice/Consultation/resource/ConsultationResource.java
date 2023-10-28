package com.digitalholics.consultationsservice.Consultation.resource;

import com.digitalholics.consultationsservice.Consultation.resource.Patient.PatientResource;
import com.digitalholics.consultationsservice.Consultation.resource.Physiotherapist.PhysiotherapistResource;

import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationResource {

    private Integer id;
    private Boolean done;
    private String topic;
    private String diagnosis;
    private String date;
    private String hour;
    private String place;
    private PhysiotherapistResource physiotherapist;
    private PatientResource patient;
}
