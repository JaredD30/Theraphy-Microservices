package org.digitalholics.iotdataservice.IoTData.resource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateIotResultResource {
    private String temperature;
    private String distance;
    private String pulse;
    private String humidity;
    private Integer therapyId;
    private String date;
}
