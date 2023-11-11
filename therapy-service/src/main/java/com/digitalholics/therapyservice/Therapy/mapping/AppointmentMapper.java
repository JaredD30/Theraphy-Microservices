package com.digitalholics.therapyservice.Therapy.mapping;

import com.digitalholics.therapyservice.Shared.EnhancedModelMapper;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.Appointment;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.AppointmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.CreateAppointmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.UpdateAppointmentResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;


public class AppointmentMapper implements Serializable {

    @Autowired
    EnhancedModelMapper mapper;

    public AppointmentResource toResource(Appointment model){
        return mapper.map(model, AppointmentResource.class);
    }

    public Appointment toModel(CreateAppointmentResource resource) {
        return mapper.map(resource, Appointment.class);
    }

    public Appointment toModel(UpdateAppointmentResource resource) { return mapper.map(resource, Appointment.class);}

    public Page<AppointmentResource> modelListPage(List<Appointment> modelList, Pageable pageable) {
        return new PageImpl<>(mapper.mapList(modelList, AppointmentResource.class), pageable, modelList.size());
    }




}
