package com.digitalholics.echoservice.client.controller;



import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.UUID;



@RestController
@RequestMapping("/echo")
public class EchoServiceController {

    @Value("${server.port}")
    private String port;

    private final String instanceId = UUID.randomUUID().toString();


    @GetMapping(value ="/")
    public String echoService(){

        return "Miau from " + instanceId;
    }

    @GetMapping("/{id}")
    public String echoService(@PathVariable String id){
        return id  + " from port "+ port;
    }

}
