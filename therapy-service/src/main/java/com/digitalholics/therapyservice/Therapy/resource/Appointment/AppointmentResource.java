package com.digitalholics.therapyservice.Therapy.resource.Appointment;

import com.digitalholics.therapyservice.Therapy.resource.Therapy.TherapyResource;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResource {

    private Integer id;
    private Boolean done;
    private String topic;
    private String diagnosis;
    private String date;
    private String hour;
    private String place;
    private TherapyResource therapy;
}
