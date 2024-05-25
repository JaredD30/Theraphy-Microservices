package org.digitalholics.iotdataservice.IoTData.domain.service;


import org.digitalholics.iotdataservice.IoTData.domain.model.entity.IotDevice;
import org.digitalholics.iotdataservice.IoTData.resource.CreateIotDeviceResource;
import org.digitalholics.iotdataservice.IoTData.resource.UpdateIotDeviceResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IotDeviceService {
    List<IotDevice> getAll();
    Page<IotDevice> getAll(Pageable pageable);

    List<IotDevice> getByTherapyIdAndDate(Integer therapyId, String date);
    IotDevice getById(Integer iotDeviceId);
    IotDevice getByTemperature(String temperature);
    IotDevice create(CreateIotDeviceResource iotDeviceResource);
    IotDevice update(Integer iotDeviceId, UpdateIotDeviceResource request);
    ResponseEntity<?> delete(Integer iotDeviceId);
}
