package com.digitalholics.therapyservice.Therapy.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Boolean done;

    @NotNull
    @NotBlank
    private String topic;

    @NotNull
    private String diagnosis;

    @NotNull
    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String date;

    @NotNull
    @NotBlank
    private String hour;

    @NotNull
    @NotBlank
    private String place;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "therapy_id", nullable = false)
    private Therapy therapy;

}
