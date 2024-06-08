package org.digitalholics.iotdataservice.IoTData.domain.service;


import org.digitalholics.iotdataservice.IoTData.domain.model.entity.IotDevice;
import org.digitalholics.iotdataservice.IoTData.resource.CreateIotDeviceResource;
import org.digitalholics.iotdataservice.IoTData.resource.IotDeviceResource;
import org.digitalholics.iotdataservice.IoTData.resource.UpdateIotDeviceResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IotDeviceService {
    List<IotDevice> getAll();
    Page<IotDeviceResource> getAllResources(String jwt, Pageable pageable);
    Page<IotDeviceResource> getAllResourcesByPhysiotherapistId(String jwt, Integer physiotherapistId, Pageable pageable);
    Page<IotDevice> getAll(Pageable pageable);
    IotDevice getById(Integer iotDeviceId);
    List<IotDevice> getByTherapyId(Integer therapyId);
    IotDevice create();
    IotDevice update(Integer iotDeviceId, UpdateIotDeviceResource request);
    ResponseEntity<?> delete(Integer iotDeviceId);
    IotDevice assignTherapy(Integer iotDeviceId, Integer therapyId);

}

