package org.digitalholics.iotdataservice.IoTData.resource;

import lombok.Getter;
import lombok.Setter;
import org.digitalholics.iotdataservice.IoTData.domain.model.entity.External.Therapy;

@Getter
@Setter
public class UpdateIotDeviceResource {
    private String assignmentDate;
    private Integer therapyQuantity;
}
