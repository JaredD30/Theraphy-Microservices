package org.digitalholics.iotdataservice.IoTData.domain.service;


import org.digitalholics.iotdataservice.IoTData.domain.model.entity.IotResult;
import org.digitalholics.iotdataservice.IoTData.resource.CreateIotResultResource;
import org.digitalholics.iotdataservice.IoTData.resource.UpdateIotResultResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IotResultService {

    List<IotResult> getAll();
    Page<IotResult> getAll(Pageable pageable);
    IotResult getById(Integer resultId);
    IotResult getByTherapyId(String therapyId);
    List<IotResult> getByTherapyIdAndDate(String therapyId, String date);
    IotResult create(CreateIotResultResource resultResource);
    IotResult update(Integer resultId, UpdateIotResultResource request);
    ResponseEntity<?> delete(Integer resultId);

}
