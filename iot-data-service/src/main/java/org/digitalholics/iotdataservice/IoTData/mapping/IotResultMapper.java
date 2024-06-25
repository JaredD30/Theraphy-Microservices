package org.digitalholics.iotdataservice.IoTData.mapping;


import org.digitalholics.iotdataservice.IoTData.domain.model.entity.IotResult;
import org.digitalholics.iotdataservice.IoTData.resource.CreateIotResultResource;
import org.digitalholics.iotdataservice.IoTData.resource.IotResultResource;
import org.digitalholics.iotdataservice.IoTData.resource.UpdateIotResultResource;
import org.digitalholics.iotdataservice.Shared.EnhancedModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class IotResultMapper implements Serializable {

    @Autowired
    EnhancedModelMapper mapper;

    public IotResultResource toResource(IotResult model) {
        return mapper.map(model, IotResultResource.class);
    }

    public IotResult toModel(CreateIotResultResource resource) {
        return mapper.map(resource, IotResult.class);
    }

    public IotResult toModel(UpdateIotResultResource resource) {
        return mapper.map(resource, IotResult.class);
    }

    public Page<IotResultResource> modelListPage(List<IotResult> modelList, Pageable pageable) {
        return new PageImpl<>(mapper.mapList(modelList, IotResultResource.class), pageable, modelList.size());
    }

}
