package org.digitalholics.iotdataservice.IoTData.api.rest;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.digitalholics.iotdataservice.IoTData.domain.service.IotDeviceService;
import org.digitalholics.iotdataservice.IoTData.mapping.IotDeviceMapper;
import org.digitalholics.iotdataservice.IoTData.resource.CreateIotDeviceResource;
import org.digitalholics.iotdataservice.IoTData.resource.IotDeviceResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/iot-data/iotDevice", produces = "application/json")
@Tag(name = "IoTDevice", description = "Create, read, update and delete iotDevice")
public class IotDeviceController {

    private final IotDeviceService iotDeviceService;

    private final IotDeviceMapper mapper;

    public IotDeviceController(IotDeviceService iotDeviceService, IotDeviceMapper mapper) {
        this.iotDeviceService = iotDeviceService;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<IotDeviceResource> getAllIoTDevices(@Parameter(hidden = true) @RequestHeader("Authorization") String jwt, Pageable pageable) {
        return iotDeviceService.getAllResources(jwt, pageable);
    }

    @GetMapping("/byPhysiotherapistId/{physiotherapistId}")
    public Page<IotDeviceResource> getIoTDevicesByPhysiotherapistId(@Parameter(hidden = true) @RequestHeader("Authorization") String jwt, @PathVariable Integer physiotherapistId, Pageable pageable) {
        return iotDeviceService.getAllResourcesByPhysiotherapistId(jwt,physiotherapistId, pageable);
    }

    @GetMapping("{iotDeviceId}")
    public IotDeviceResource getIoTDeviceById(@PathVariable Integer iotDeviceId) {
        return mapper.toResource(iotDeviceService.getById(iotDeviceId));
    }


    @PostMapping
    public ResponseEntity<IotDeviceResource> createIoTDevice() {
        return new ResponseEntity<>(mapper.toResource(iotDeviceService.create()), HttpStatus.CREATED);
    }

    @PostMapping("/assign/{iotDeviceId}/to/{therapyId}")
    public ResponseEntity<IotDeviceResource> assignToTherapy(@PathVariable Integer iotDeviceId, @PathVariable Integer therapyId) {
        return new ResponseEntity<>(mapper.toResource(iotDeviceService.assignTherapy(iotDeviceId,therapyId)), HttpStatus.CREATED);
    }



    @DeleteMapping("{iotDeviceId}")
    public ResponseEntity<?> deleteIotDevice(@PathVariable Integer iotDeviceId) {
        return iotDeviceService.delete(iotDeviceId);
    }
}
