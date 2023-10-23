package com.digitalholics.profileservice.Profile.mapping;

import com.digitalholics.profileservice.Profile.domain.model.entity.Physiotherapist;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.CreatePhysiotherapistResource;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.PhysiotherapistResource;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.UpdatePhysiotherapistResource;
import com.digitalholics.profileservice.Shared.EnhancedModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class PhysiotherapistMapper implements Serializable {

    @Autowired
    EnhancedModelMapper mapper;

    public PhysiotherapistResource toResource(Physiotherapist model) {
        return mapper.map(model, PhysiotherapistResource.class);
    }

    public Physiotherapist toModel(CreatePhysiotherapistResource resource) {
        return mapper.map(resource, Physiotherapist.class);
    }

    public Physiotherapist toModel(UpdatePhysiotherapistResource resource) { return mapper.map(resource, Physiotherapist.class);}

    public Page<PhysiotherapistResource> modelListPage(List<Physiotherapist> modelList, Pageable pageable) {
        return new PageImpl<>(mapper.mapList(modelList, PhysiotherapistResource.class), pageable, modelList.size());
    }



}
