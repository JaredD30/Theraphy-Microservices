package org.digitalholics.iotdataservice.IoTData.resource;

import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class CreateIotDeviceResource {
    private Integer id;
    private String temperature;
    private String distance;
    private String pulse;
    private String humidity;
    private Integer therapyId;
}
