package org.digitalholics.iotdataservice.IoTData.api.rest;


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
    public Page<IotDeviceResource> getAllIoTDevices(Pageable pageable) {
        return mapper.modelListPage(iotDeviceService.getAll(), pageable);
    }

    @GetMapping("byTherapyId/{therapyId}/Date/{date}")
    public Page<IotDeviceResource> getByTherapyIdAndDate(@PathVariable Integer therapyId,@PathVariable String date, Pageable pageable) {
        return mapper.modelListPage(iotDeviceService.getByTherapyIdAndDate(therapyId, date), pageable);
    }

    @GetMapping("{iotDeviceId}")
    public IotDeviceResource getIoTDeviceById(@PathVariable Integer iotDeviceId) {
        return mapper.toResource(iotDeviceService.getById(iotDeviceId));
    }

    @GetMapping("iotDevice/{temperature}")
    public IotDeviceResource getIoTDeviceByPatientId(@PathVariable String temperature) {
        return mapper.toResource(iotDeviceService.getByTemperature(temperature));
    }

    @PostMapping
    public ResponseEntity<IotDeviceResource> createIoTDevice(@RequestBody CreateIotDeviceResource resource) {
        return new ResponseEntity<>(mapper.toResource(iotDeviceService.create(resource)), HttpStatus.CREATED);
    }

    @DeleteMapping("{iotDeviceId}")
    public ResponseEntity<?> deleteIotDevice(@PathVariable Integer iotDeviceId) {
        return iotDeviceService.delete(iotDeviceId);
    }
}
