package org.digitalholics.iotdataservice.IoTData.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "iotDevice")
public class IotDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String temperature;
    private String distance;
    private String pulse;
    private String humidity;

    private Integer therapyId;
    private String date;
}

