package com.digitalholics.therapyservice.Therapy.mapping;

import com.digitalholics.therapyservice.Shared.EnhancedModelMapper;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.Treatment;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.CreateTreatmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.TreatmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.UpdateTreatmentResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class TreatmentMapper implements Serializable {
    @Autowired
    EnhancedModelMapper mapper;

    public TreatmentResource toResource(Treatment model){
        return mapper.map(model, TreatmentResource.class);
    }

    public Treatment toModel(CreateTreatmentResource resource) {
        return mapper.map(resource, Treatment.class);
    }

    public Treatment toModel(UpdateTreatmentResource resource) { return mapper.map(resource, Treatment.class);}

    public Page<TreatmentResource> modelListPage(List<Treatment> modelList, Pageable pageable) {
        return new PageImpl<>(mapper.mapList(modelList, TreatmentResource.class), pageable, modelList.size());
    }
}
