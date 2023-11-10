package com.digitalholics.profileservice.Profile.domain.service;


import com.digitalholics.profileservice.Profile.domain.model.entity.External.User;
import com.digitalholics.profileservice.Profile.domain.model.entity.Physiotherapist;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.CreatePhysiotherapistResource;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.UpdatePhysiotherapistResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.sql.RowSet;
import java.util.List;

public interface PhysiotherapistService {
    List<Physiotherapist> getAll();
    Page<Physiotherapist> getAll(Pageable pageable);
    Physiotherapist getById(Integer patientId);
    Physiotherapist getByUserId(Integer userId);
    Physiotherapist getLoggedInPhysiotherapist(String jwt);
    Physiotherapist create(String jwt, CreatePhysiotherapistResource physiotherapist);
    Physiotherapist update(String jwt, Integer physiotherapistId, UpdatePhysiotherapistResource request);
    ResponseEntity<?> delete(String jwt, Integer physiotherapistId);
    User validateJwtAndGetUser(String jwt);
}
