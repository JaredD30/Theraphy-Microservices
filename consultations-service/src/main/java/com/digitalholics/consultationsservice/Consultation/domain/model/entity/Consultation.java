package com.digitalholics.consultationsservice.Consultation.domain.model.entity;



import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Patient;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Physiotherapist;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "consultations")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Boolean done;

    @NotNull
    @NotBlank
    private String topic;

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

    private Integer physiotherapistId;

    private Integer patientId;
}
