package org.digitalholics.iotdataservice.IoTData.domain.model.entity.External;


import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Physiotherapist {
    private Integer id;
    private String dni;
    private String specialization;
    private Integer age;
    private String location;
    private String photoUrl;
    private String birthdayDate;
    private Double rating;
    private Integer consultationQuantity;
    private Integer patientQuantity;
    private Integer yearsExperience;
    private Double fees;
    private User user;
}
