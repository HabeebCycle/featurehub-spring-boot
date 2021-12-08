package com.example.demo.config;

import io.featurehub.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Configuration
public class FeatureConfig {


    private static final Logger LOG = LoggerFactory.getLogger(FeatureConfig.class);

    private final String featureHubHost;
    private final String featureHubApiKey;

    public FeatureConfig(@Value("${featurehub.edge.url}") final String featureHubHost, @Value("${featurehub.api.key}") final String featureHubApiKey) {
        this.featureHubHost = featureHubHost;
        this.featureHubApiKey = featureHubApiKey;
    }

    @Bean
    public FeatureHubConfig featureHubConfig() throws ExecutionException, InterruptedException {
        if(featureHubHost == null) {
            throw new RuntimeException("Unable to determine the feature hub host.");
        }

        if(featureHubApiKey == null) {
            throw new RuntimeException("Unable to determine the API key for FeatureHub");
        }

        FeatureHubConfig config = new EdgeFeatureHubConfig(featureHubHost, featureHubApiKey);
        config.init();

        Future<ClientContext> contextFuture = config.newContext().build();

        contextFuture.get().feature("bff_config").addListener(fs -> LOG.info(fs.getRawJson()));

        config.addReadynessListener(readyness -> {
            if(readyness == Readyness.Ready)
                LOG.info("Ready to connect!");
        });

        return config;
    }

    /*@Bean
    @Scope("request")
    public ClientContext featureHubClient(FeatureHubConfig config, HttpServletRequest request) {
        ClientContext clientContext = config.newContext();

        if(request.getHeader("Authorization") != null) {
            clientContext.userKey(request.getHeader("Authorization"));
        }

        return clientContext;
    }*/
}
