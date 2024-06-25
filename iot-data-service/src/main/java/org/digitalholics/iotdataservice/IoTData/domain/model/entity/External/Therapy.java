package org.digitalholics.iotdataservice.IoTData.domain.model.entity.External;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Therapy {
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

