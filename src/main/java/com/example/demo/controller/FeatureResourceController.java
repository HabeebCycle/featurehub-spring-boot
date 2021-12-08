package com.example.demo.controller;

import com.example.demo.model.DemoClass;
import io.featurehub.client.ClientContext;
import io.featurehub.client.FeatureHubConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/features")
public class FeatureResourceController {

    private final ClientContext clientContext;

    public FeatureResourceController(FeatureHubConfig config) {
        this.clientContext = config.newContext();
    }

    @GetMapping("/toggles")
    public Boolean toggles() {
        return clientContext.feature("capitalize").getBoolean();
    }

    @GetMapping("/json")
    public String jsons() {
        return clientContext.feature("bff_config").getRawJson();
    }

    @GetMapping("/appconfig")
    public DemoClass appconfig() {
        return clientContext.feature("app_config").getJson(DemoClass.class);
    }

    @GetMapping("/values")
    public String values() {
        return clientContext.feature("kafka_key").getString();
    }
}
