package org.digitalholics.iotdataservice.IoTData.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.digitalholics.iotdataservice.IoTData.domain.model.entity.IotDevice;
import org.digitalholics.iotdataservice.IoTData.domain.persistence.IotDeviceRepository;
import org.digitalholics.iotdataservice.IoTData.domain.service.IotDeviceService;
import org.digitalholics.iotdataservice.IoTData.mapping.IotDeviceMapper;
import org.digitalholics.iotdataservice.IoTData.resource.CreateIotDeviceResource;
import org.digitalholics.iotdataservice.IoTData.resource.IotDeviceResource;
import org.digitalholics.iotdataservice.IoTData.resource.UpdateIotDeviceResource;
import org.digitalholics.iotdataservice.Shared.Exception.ResourceNotFoundException;
import org.digitalholics.iotdataservice.Shared.Exception.ResourceValidationException;
import org.digitalholics.iotdataservice.Shared.configuration.ExternalConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IotDeviceServiceImpl implements IotDeviceService {

    private static final String ENTITY = "IoTDevice";

    private final IotDeviceRepository iotDeviceRepository;
    private final Validator validator;
    private final ExternalConfiguration externalConfiguration;
    private final IotDeviceMapper mapper;

    public IotDeviceServiceImpl(IotDeviceRepository iotDeviceRepository, Validator validator, ExternalConfiguration externalConfiguration, IotDeviceMapper mapper) {
        this.iotDeviceRepository = iotDeviceRepository;
        this.validator = validator;
        this.externalConfiguration = externalConfiguration;
        this.mapper = mapper;
    }

    @Override
    public List<IotDevice> getAll() {
        return iotDeviceRepository.findAll();
    }

    @Override
    public Page<IotDeviceResource> getAllResources(String jwt, Pageable pageable) {
        Page<IotDeviceResource> iotdevices =
                mapper.modelListPage(getAll(), pageable);
        iotdevices.forEach(iotDeviceResource -> {
            iotDeviceResource.setTherapy(externalConfiguration.getTheraphyByID(jwt, iotDeviceResource.getTherapy().getId()));

        });

        return iotdevices;
    }
    @Override
    public Page<IotDeviceResource> getAllResourcesByPhysiotherapistId(String jwt, Integer physiotherapistId, Pageable pageable) {
        Page<IotDeviceResource> iotDevices = mapper.modelListPage(getAll(), pageable);

        // Filtrar dispositivos que pertenecen al fisioterapeuta especificado
        List<IotDeviceResource> filteredDevices = iotDevices.stream()
                .filter(iotDeviceResource -> {
                    // Actualizar la terapia para cada dispositivo
                    iotDeviceResource.setTherapy(externalConfiguration.getTheraphyByID(jwt, iotDeviceResource.getTherapy().getId()));
                    // Filtrar por physiotherapistId
                    return iotDeviceResource.getTherapy().getPhysiotherapist().getId().equals(physiotherapistId);
                })
                .collect(Collectors.toList());

        // Convertir la lista filtrada de vuelta a una página
        Page<IotDeviceResource> iotdevicesfilteredPage = new PageImpl<>(filteredDevices, pageable, filteredDevices.size());

        return iotdevicesfilteredPage;
    }

    @Override
    public Page<IotDevice> getAll(Pageable pageable) {
        return iotDeviceRepository.findAll(pageable);
    }

    @Override
    public IotDevice getById(Integer iotDeviceId) {
        return iotDeviceRepository.findById(iotDeviceId)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, iotDeviceId));
    }

    @Override
    public List<IotDevice> getByTherapyId(Integer therapyId) {
        return iotDeviceRepository.findByTherapyId(therapyId);
    }

    @Override
    public IotDevice create( ) {
        IotDevice iotDevice = new IotDevice();

        iotDevice.setTherapyId(0);
        iotDevice.setAssignmentDate("New");
        iotDevice.setTherapyQuantity(0);
        return iotDeviceRepository.save(iotDevice);
    }

    @Override
    public IotDevice update(Integer iotDeviceId, UpdateIotDeviceResource request) {
        IotDevice iotDevice = getById(iotDeviceId);

        if (request.getAssignmentDate() != null) {
            iotDevice.setAssignmentDate(request.getAssignmentDate());
        }
        if (request.getTherapyQuantity() != null) {
            iotDevice.setTherapyQuantity(request.getTherapyQuantity());
        }

        return iotDeviceRepository.save(iotDevice);
    }

    @Override
    public ResponseEntity<?> delete(Integer iotDeviceId) {
        return iotDeviceRepository.findById(iotDeviceId).map(iotDevice -> {
            iotDeviceRepository.delete(iotDevice);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,iotDeviceId));
    }

        @Override
        public IotDevice assignTherapy(Integer iotDeviceId, Integer therapyId) {
            IotDevice iotDevice = getById(iotDeviceId);

            iotDevice.setTherapyId(therapyId);
            String currentDate = LocalDate.now().toString();
            iotDevice.setAssignmentDate(currentDate);

            Integer beforeTherapyQuantity = iotDevice.getTherapyQuantity();

            if (beforeTherapyQuantity +1 >5){
                throw new RuntimeException("El dispositivo ha llegado a su límite de asignaciones de terapia.");
            }else{
                iotDevice.setTherapyQuantity(beforeTherapyQuantity+1);

                return iotDeviceRepository.save(iotDevice);
            }
        }


}
