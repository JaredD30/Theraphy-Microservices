    package com.digitalholics.healthexpertiseservice.HealthExpertise.service;


    import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Certification;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Physiotherapist;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.User;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.CertificationRepository;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External.PhysiotherapistRepository;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External.UserRepository;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.CertificationService;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.CreateCertificationResource;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.UpdateCertificationResource;
    import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceNotFoundException;
    import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceValidationException;
    import com.digitalholics.healthexpertiseservice.Shared.JwtValidation.JwtValidator;
    import jakarta.validation.ConstraintViolation;
    import jakarta.validation.Validator;
    import jakarta.ws.rs.NotFoundException;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.HttpEntity;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpMethod;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestTemplate;


    import java.util.List;
    import java.util.Objects;
    import java.util.Optional;
    import java.util.Set;

    @Service
    public class CertificationServiceImpl implements CertificationService {

        private static final String ENTITY = "Certification";

        private final CertificationRepository certificationRepository;
        private final PhysiotherapistRepository physiotherapistRepository;

        private final JwtValidator jwtValidator;

        private final Validator validator;

        public CertificationServiceImpl(CertificationRepository certificationRepositoryRepository, PhysiotherapistRepository physiotherapistRepository, JwtValidator jwtValidator, Validator validator) {
            this.certificationRepository = certificationRepositoryRepository;
            this.physiotherapistRepository = physiotherapistRepository;
            this.jwtValidator = jwtValidator;
            this.validator = validator;
        }

        @Override
        public List<Certification> getAll() {
            return certificationRepository.findAll();
        }

        @Override
        public Page<Certification> getAll(Pageable pageable) {
            return certificationRepository.findAll(pageable);
        }

        @Override
        public Certification getById(Integer certificationId) {
            return certificationRepository.findById(certificationId)
                    .orElseThrow(()-> new ResourceNotFoundException(ENTITY, certificationId));
        }

        @Override
        public List<Certification> getByPhysiotherapistId(Integer physiotherapistId) {
            List<Certification> certifications = certificationRepository.findByPhysiotherapistId(physiotherapistId);

            if(certifications.isEmpty())
                throw new ResourceValidationException(ENTITY,
                        "Not found Certifications for this physiotherapist");

            return certifications;
        }

        @Override
        public Certification create(String jwt, CreateCertificationResource certificationResource) {

            Set<ConstraintViolation<CreateCertificationResource>> violations = validator.validate(certificationResource);

            if (!violations.isEmpty())
                throw new ResourceValidationException(ENTITY, violations);

            User user = jwtValidator.validateJwtAndGetUser(jwt, "PHYSIOTHERAPIST");

            Optional<Physiotherapist> physiotherapistOptional = Optional.ofNullable(physiotherapistRepository.findPhysiotherapistByUserUsername(user.getUsername()));
            Physiotherapist physiotherapist = physiotherapistOptional.orElseThrow(() -> new NotFoundException("Not found patient with email: " + user.getUsername()));

            Certification certification = new Certification();
            certification.setPhysiotherapist(physiotherapist);
            certification.setTitle(certificationResource.getTitle());
            certification.setSchool(certificationResource.getSchool());
            certification.setYear(certificationResource.getYear());

            return certificationRepository.save(certification);
        }

        @Override
        public Certification update(String jwt, Integer certificationId, UpdateCertificationResource request) {
            User user = jwtValidator.validateJwtAndGetUser(jwt, "PHYSIOTHERAPIST");

            Certification certification = getById(certificationId);

            if(Objects.equals(user.getUsername(), certification.getPhysiotherapist().getUser().getUsername()) || Objects.equals(String.valueOf(user.getRole()), "ADMIN")){
                if (request.getTitle() != null) {
                    certification.setTitle(request.getTitle());
                }
                if (request.getSchool() != null) {
                    certification.setSchool(request.getSchool());
                }
                if (request.getYear() != null) {
                    certification.setYear(request.getYear());
                }

                return certificationRepository.save(certification);
            }

            throw new ResourceValidationException("JWT",
                    "Invalid access.");
        }

        @Override
        public ResponseEntity<?> delete(String jwt, Integer certificationId) {
            User user = jwtValidator.validateJwtAndGetUser(jwt, "PHYSIOTHERAPIST");

            return certificationRepository.findById(certificationId).map(certification -> {
                if(Objects.equals(user.getUsername(), certification.getPhysiotherapist().getUser().getUsername()) || Objects.equals(String.valueOf(user.getRole()), "ADMIN")){

                    certificationRepository.delete(certification);
                    return ResponseEntity.ok().build();
                }
                throw new ResourceValidationException("JWT",
                        "Invalid access.");
            }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,certificationId));
        }

    }
