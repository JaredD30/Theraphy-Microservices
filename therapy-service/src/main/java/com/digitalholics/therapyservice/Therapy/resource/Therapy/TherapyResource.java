package com.digitalholics.therapyservice.Therapy.resource.Therapy;

import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Patient;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Physiotherapist;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class TherapyResource {
    private Integer id;
    private String therapyName;
    private String description;
    private Integer appointmentQuantity;
    private String startAt;
    private String finishAt;
    private Boolean finished;
    private Patient patient;
    private Physiotherapist physiotherapist;
}
