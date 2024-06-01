package com.digitalholics.consultationsservice.Consultation.resource;

import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Patient;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Physiotherapist;
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
    private Physiotherapist physiotherapistId;
    private Patient patientId;
}
