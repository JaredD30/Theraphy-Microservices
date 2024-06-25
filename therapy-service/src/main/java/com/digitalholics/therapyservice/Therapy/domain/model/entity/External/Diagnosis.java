package com.digitalholics.therapyservice.Therapy.domain.model.entity.External;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Diagnosis {
    private Integer id;
    private Integer patientId;
    private String diagnosis;
    private String date;
}
