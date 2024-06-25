package org.digitalholics.iotdataservice.IoTData.resource;

import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class IotResultResource {
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
