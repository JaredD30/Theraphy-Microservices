package com.digitalholics.therapyservice.Therapy.resource.Treatment;

import com.digitalholics.therapyservice.Therapy.resource.Therapy.TherapyResource;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentResource {

    private Integer id;
    private String videoUrl;
    private String duration;
    private String title;
    private String description;
    private String day;
    private Boolean viewed;
    private TherapyResource therapy;

}
