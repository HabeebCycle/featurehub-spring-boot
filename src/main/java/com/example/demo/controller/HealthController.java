package com.example.demo.controller;

import io.featurehub.client.FeatureHubConfig;
import io.featurehub.client.Readyness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/health")
public class HealthController {
    private static final Logger LOG = LoggerFactory.getLogger(HealthController.class);
    private final FeatureHubConfig featureHubConfig;

    public HealthController(FeatureHubConfig featureHubConfig) {
        this.featureHubConfig = featureHubConfig;
    }

    @GetMapping("/liveness")
    public String livenessMapping() {
        if(featureHubConfig.getReadyness() == Readyness.Ready) {
            return "FeatureHub UP and Running";
        }

        LOG.warn("FeatureHub connection not yet available, reporting not live.");
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
