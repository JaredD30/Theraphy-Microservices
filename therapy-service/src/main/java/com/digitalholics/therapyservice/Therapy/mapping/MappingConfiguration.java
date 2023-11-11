package com.digitalholics.therapyservice.Therapy.mapping;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("therapiesTreatmentsMappingConfiguration")
public class MappingConfiguration {

    @Bean
    public TherapyMapper therapyMapper() { return new TherapyMapper();}
    @Bean
    public TreatmentMapper treatmentMapper() { return new TreatmentMapper();}
    @Bean
    public AppointmentMapper appointmentMapper() { return new AppointmentMapper();}
}
