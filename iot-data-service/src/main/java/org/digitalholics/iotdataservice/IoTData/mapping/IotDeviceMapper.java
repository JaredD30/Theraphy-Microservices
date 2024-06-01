package org.digitalholics.iotdataservice.IoTData.mapping;


import org.digitalholics.iotdataservice.IoTData.domain.model.entity.IotDevice;
import org.digitalholics.iotdataservice.IoTData.resource.CreateIotDeviceResource;
import org.digitalholics.iotdataservice.IoTData.resource.IotDeviceResource;
import org.digitalholics.iotdataservice.IoTData.resource.UpdateIotDeviceResource;
import org.digitalholics.iotdataservice.Shared.EnhancedModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class IotDeviceMapper implements Serializable {
    @Autowired
    EnhancedModelMapper mapper;

    public IotDeviceResource toResource(IotDevice model) {
        return mapper.map(model, IotDeviceResource.class);
    }

    public IotDevice toModel(CreateIotDeviceResource resource) {
        return mapper.map(resource, IotDevice.class);
    }

    public IotDevice toModel(UpdateIotDeviceResource resource) {
        return mapper.map(resource, IotDevice.class);
    }

    public Page<IotDeviceResource> modelListPage(List<IotDevice> modelList, Pageable pageable) {
        return new PageImpl<>(mapper.mapList(modelList, IotDeviceResource.class), pageable, modelList.size());
    }
}
