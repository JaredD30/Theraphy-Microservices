package com.digitalholics.socialservice.Social.resource;

import com.digitalholics.socialservice.Social.domain.model.entity.External.Patient;
import com.digitalholics.socialservice.Social.domain.model.entity.External.Physiotherapist;
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
    private Physiotherapist physiotherapist;
    private Patient patient;
}
