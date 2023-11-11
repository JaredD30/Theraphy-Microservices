package com.digitalholics.healthexpertiseservice.HealthExpertise.mapping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("healthAndExpertiseMappingConfiguration") //del bounded context
public class MappingConfiguration {
    @Bean
    public CertificationMapper certificationMapper() { return new CertificationMapper();}

    @Bean
    public JobMapper jobMapper() { return new JobMapper();}

    @Bean
    public DiagnosisMapper diagnosisMapper() { return new DiagnosisMapper();}

    @Bean
    public MedicalHistoryMapper medicalHistoryMapper() { return new MedicalHistoryMapper();}
}
