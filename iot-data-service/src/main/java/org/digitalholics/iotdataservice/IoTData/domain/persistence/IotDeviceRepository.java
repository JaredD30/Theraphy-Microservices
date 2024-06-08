package org.digitalholics.iotdataservice.IoTData.domain.persistence;


import org.digitalholics.iotdataservice.IoTData.domain.model.entity.IotDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IotDeviceRepository extends JpaRepository<IotDevice, Integer> {

    List<IotDevice>  findByTherapyId(Integer therapyId);




}
