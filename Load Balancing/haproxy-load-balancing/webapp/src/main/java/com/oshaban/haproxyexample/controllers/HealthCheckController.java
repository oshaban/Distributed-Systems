package com.oshaban.haproxyexample.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/health")
    public String health() {
        return "All good from: " + serverPort;
    }

}
