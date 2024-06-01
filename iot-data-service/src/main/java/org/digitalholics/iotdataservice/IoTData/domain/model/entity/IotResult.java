package org.digitalholics.iotdataservice.IoTData.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "iotResults")
public class IotResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String iotDeviceId;
    private String humidity;
    private String temperature;
    private String pulse;
    private String mapAmplitude;
    private String mapFrequency;
    private String mapDuration;
    private String date;
}
