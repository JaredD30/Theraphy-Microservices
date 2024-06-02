    package com.digitalholics.healthexpertiseservice.HealthExpertise.service;




    import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Certification;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Physiotherapist;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.User;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.CertificationRepository;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.CertificationService;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.CreateCertificationResource;
    import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.UpdateCertificationResource;
    import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceNotFoundException;
    import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceValidationException;
    import com.digitalholics.healthexpertiseservice.Shared.configuration.ExternalConfiguration;
    import jakarta.validation.ConstraintViolation;
    import jakarta.validation.Validator;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Service;


    import java.util.List;
    import java.util.Set;

    @Service
    public class CertificationServiceImpl implements CertificationService {

        private static final String ENTITY = "Certification";
        private final CertificationRepository certificationRepository;
        private final Validator validator;
        private final ExternalConfiguration externalConfiguration;

        public CertificationServiceImpl(CertificationRepository certificationRepositoryRepository, Validator validator, ExternalConfiguration externalConfiguration) {
            this.certificationRepository = certificationRepositoryRepository;
            this.validator = validator;
            this.externalConfiguration = externalConfiguration;
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

            User user = externalConfiguration.getUser(jwt);
            Physiotherapist physiotherapist =  externalConfiguration.getPhysiotherapistByUserId(jwt, user.getId());
            Certification certification = new Certification();
            certification.setPhysiotherapistId(physiotherapist.getId());
            certification.setTitle(certificationResource.getTitle());
            certification.setSchool(certificationResource.getSchool());
            certification.setYear(certificationResource.getYear());

            return certificationRepository.save(certification);
        }

        @Override
        public Certification update(String jwt, Integer certificationId, UpdateCertificationResource request) {

            Certification certification = getById( certificationId);

            if(certification == null)
                throw new ResourceValidationException(ENTITY,
                        "Not found Medical History with ID:"+ certificationId);

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

        @Override
        public Certification updateTitleSchoolYear(String jwt, Integer certificationId, UpdateCertificationResource request) {

            Certification certification = getById( certificationId);

            if(certification == null)
                throw new ResourceValidationException(ENTITY,
                        "Not found Medical History with ID:"+ certificationId);

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

        @Override
        public ResponseEntity<?> delete(String jwt, Integer certificationId) {

            return certificationRepository.findById(certificationId).map(certification -> {
                certificationRepository.delete(certification);
                return ResponseEntity.ok().build();
            }).orElseThrow(() -> new ResourceNotFoundException(ENTITY, certificationId));
        }

    }
