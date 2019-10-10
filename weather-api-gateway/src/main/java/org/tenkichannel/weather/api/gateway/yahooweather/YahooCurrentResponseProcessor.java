package org.tenkichannel.weather.api.gateway.yahooweather;

import java.lang.invoke.MethodHandles;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tenkichannel.weather.api.gateway.domain.Weather;
import org.tenkichannel.weather.api.gateway.yahooweather.model.Condition;
import org.tenkichannel.weather.api.gateway.yahooweather.model.YahooQueryResponse;

@ApplicationScoped
public class YahooCurrentResponseProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    ObjectMapper mapper;

    ObjectReader reader;

    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.debug("Starting to handle response from server");
        final String weatherDataJson = exchange.getIn().getBody(String.class);
        LOGGER.debug("Weather data dump: {}", weatherDataJson);
        final YahooQueryResponse response = reader.forType(YahooQueryResponse.class).readValue(weatherDataJson);
        final Weather weather = new Weather();

        try {
            Condition condition = response.getCurrent_observation().getCondition();
            // get condition, rain, clear, sunny, etc..
            weather.setCondition(condition.getText());
        } catch (final Exception e) {
            LOGGER.error(e.getMessage());
            weather.setCondition("");
        }

        LOGGER.debug("Final weather data is {} and provider is {}", weather, "Yahoo Weather");
        exchange.getIn().setBody(weather, Weather.class);
    }
}
