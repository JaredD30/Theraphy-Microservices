package com.digitalholics.socialservice.Social.resource;

import com.digitalholics.socialservice.Social.resource.Extermal.PatientResource;
import com.digitalholics.socialservice.Social.resource.Extermal.PhysiotherapistResource;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResource {
    private Integer id;
    private String content;
    private Integer score;
    private PhysiotherapistResource physiotherapist;
    private PatientResource patient;
}
