package org.tenkichannel.weather.api.gateway.routes.openweathermap;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tenkichannel.weather.api.gateway.domain.Weather;
import org.tenkichannel.weather.api.gateway.routes.openweathermap.model.Current;

/**
 * Process the JSON Response from the service <a href="https://openweathermap.org/current">Current</a> of the OpenWeatherData API. 
 * Bind to the exchange a {@link Weather} object.
 * 
 */
@ApplicationScoped
public class CurrentResponseProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentResponseProcessor.class);

    // Jsonb don't have official support on Camel yet, but let's use it since it's the standard on Quarkus
    final private Jsonb jsonb = JsonbBuilder.create();

    public CurrentResponseProcessor() {

    }

    @Override
    public void process(Exchange exchange) {
        LOGGER.debug("Starting to handle response from server");
        final String weatherDataJson = exchange.getIn().getBody(String.class);
        LOGGER.debug("Weather data dump: {}", weatherDataJson);
        final Current current = jsonb.fromJson(weatherDataJson, Current.class);
        final Weather weather = new Weather();
        if (current.getWeather().isEmpty()) {
            weather.setCondition("");
        } else {
            weather.setCondition(current.getWeather().get(0).getMain());
        }
        LOGGER.debug("Final weather data is {}", weather);
        exchange.getIn().setBody(weather, Weather.class);
    }
}
