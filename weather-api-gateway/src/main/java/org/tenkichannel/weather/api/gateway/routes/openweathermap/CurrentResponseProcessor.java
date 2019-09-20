package org.tenkichannel.weather.api.gateway.routes.openweathermap;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.tenkichannel.weather.api.gateway.domain.Weather;
import org.tenkichannel.weather.api.gateway.routes.openweathermap.model.Current;

/**
 * Process the JSON Response from the service <a href="https://openweathermap.org/current">Current</a> of the OpenWeatherData API. 
 * Bind to the exchange a {@link Weather} object.
 * 
 */
@ApplicationScoped
public class CurrentResponseProcessor implements Processor {

    // Jsonb don't have official support on Camel yet, but let's use it since it's the standard on Quarkus
    final private Jsonb jsonb = JsonbBuilder.create();

    public CurrentResponseProcessor() {

    }

    @Override
    public void process(Exchange exchange) {
        final String weatherDataJson = exchange.getIn().getBody(String.class);
        final Current current = jsonb.fromJson(weatherDataJson, Current.class);
        final Weather weather = new Weather();
        if (current.getWeather().isEmpty()) {
            weather.setCondition("");
        } else {
            weather.setCondition(current.getWeather().get(0).getDescription());
        }
        exchange.getIn().setBody(weather, Weather.class);
    }
}
