package com.digitalholics.echoservice.client.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.UUID;



@RestController
@RequestMapping("/api/v1/echo")
@Tag(name = "Echo", description = "Echo operations: retrieval instance id information")
public class EchoServiceController {

    @Value("${server.port}")
    private String port;

    private final String instanceId = UUID.randomUUID().toString();


    @Operation(summary = "Get echo service instance id", description = "Returns echo service instance's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping(value ="/")
    public String echoService(){

        return "Miau from " + instanceId;
    }

    @Operation(summary = "Get echo service instance id with text", description = "Returns echo service instance's id with text added")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{text}")
    public String echoService(@Parameter(description = "Text", required = true, examples = @ExampleObject(name = "text", value = "Holaaaa "))@PathVariable String text){
        return text  + " from port "+ port;
    }

}
