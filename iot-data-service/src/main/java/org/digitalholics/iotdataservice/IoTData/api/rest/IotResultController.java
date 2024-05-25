package org.digitalholics.iotdataservice.IoTData.api.rest;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.digitalholics.iotdataservice.IoTData.domain.service.IotResultService;
import org.digitalholics.iotdataservice.IoTData.mapping.IotResultMapper;
import org.digitalholics.iotdataservice.IoTData.resource.CreateIotResultResource;
import org.digitalholics.iotdataservice.IoTData.resource.IotResultResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/iot-data/iotResults", produces = "application/json")
@Tag(name = "IotResult", description = "Create, read, update and delete iotResults")
public class IotResultController {

    private final IotResultService iotResultService;

    private final IotResultMapper mapper;

    public IotResultController(IotResultService iotResultService, IotResultMapper mapper) {
        this.iotResultService = iotResultService;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<IotResultResource> getAllIoTResults(Pageable pageable) {
        return mapper.modelListPage(iotResultService.getAll(), pageable);
    }

    @GetMapping("{resultId}")
    public IotResultResource getResultById(@PathVariable Integer resultId) {
        return mapper.toResource(iotResultService.getById(resultId));
    }

    @GetMapping("byTherapyId/{therapyId}")
    public IotResultResource getResultByTreatmentId(@PathVariable String therapyId) {
        return mapper.toResource(iotResultService.getByTherapyId(therapyId));
    }

    @GetMapping("byTherapyId/{therapyId}/Date/{date}")
    public Page<IotResultResource> getByTherapyIdAndDate(@PathVariable String therapyId, @PathVariable String date, Pageable pageable) {
        return mapper.modelListPage(iotResultService.getByTherapyIdAndDate(therapyId, date), pageable);
    }

    @PostMapping
    public ResponseEntity<IotResultResource> createIoTResult(@RequestBody CreateIotResultResource resource) {
        return new ResponseEntity<>(mapper.toResource(iotResultService.create(resource)), HttpStatus.CREATED);
    }

    @DeleteMapping("{resultId}")
    public ResponseEntity<?> deleteResult(@PathVariable Integer resultId) {
        return iotResultService.delete(resultId);
    }

}
