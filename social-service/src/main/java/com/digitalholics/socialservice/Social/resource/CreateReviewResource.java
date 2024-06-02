package com.digitalholics.socialservice.Social.resource;

import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewResource {
    private Integer id;
    private String content;
    private Double score;
    private Integer physiotherapistId;
}
