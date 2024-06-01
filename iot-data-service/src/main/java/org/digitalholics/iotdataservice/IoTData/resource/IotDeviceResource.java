package org.digitalholics.iotdataservice.IoTData.resource;

import lombok.*;
import org.digitalholics.iotdataservice.IoTData.domain.model.entity.External.Therapy;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class IotDeviceResource {
    private Integer id;
    private Therapy therapy;
    private String assignmentDate;
    private Integer therapyQuantity;
}
