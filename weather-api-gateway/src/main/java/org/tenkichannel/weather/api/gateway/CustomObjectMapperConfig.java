package org.tenkichannel.weather.api.gateway;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class CustomObjectMapperConfig {

    public CustomObjectMapperConfig() {
    }

    @Singleton
    @Produces
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // add any needed customizations
        return objectMapper;
    }
}
