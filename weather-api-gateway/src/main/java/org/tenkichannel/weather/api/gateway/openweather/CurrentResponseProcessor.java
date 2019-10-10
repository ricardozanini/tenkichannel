package org.tenkichannel.weather.api.gateway.openweather;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tenkichannel.weather.api.gateway.domain.Weather;
import org.tenkichannel.weather.api.gateway.openweather.model.Current;

/**
 * Process the JSON Response from the service <a href="https://openweathermap.org/current">Current</a> of the OpenWeatherData API.
 * Bind to the exchange a {@link Weather} object.
 */
@ApplicationScoped
public class CurrentResponseProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentResponseProcessor.class);

    @Inject
    ObjectMapper mapper;

    ObjectReader reader;

    public CurrentResponseProcessor() {

    }

    @PostConstruct
    public void init() {
        reader = mapper.reader();
    }

    @Override
    public void process(Exchange exchange) throws IOException {
        LOGGER.debug("Starting to handle response from server");
        final String weatherDataJson = exchange.getIn().getBody(String.class);
        LOGGER.debug("Weather data dump: {}", weatherDataJson);
        final Current current = reader.forType(Current.class).readValue(weatherDataJson);
        final Weather weather = new Weather();
        if (current.getWeather().isEmpty()) {
            weather.setCondition("");
        } else {
            weather.setCondition(current.getWeather().get(0).getMain());
        }
        LOGGER.debug("Final weather data is {} and provider is {}", weather, "OpenWeather");
        exchange.getIn().setBody(weather, Weather.class);
    }
}
