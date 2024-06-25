package org.digitalholics.iotdataservice.IoTData.mapping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("iotDeviceMappingConfiguration")
public class MappingConfiguration {
    @Bean
    public IotDeviceMapper iotDeviceMapper() { return new IotDeviceMapper();}

    @Bean
    public IotResultMapper ResultMapper() { return new IotResultMapper();}
}
