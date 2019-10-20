package org.tenkichannel.weather.api.gateway.routes;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tenkichannel.weather.api.gateway.yahooweather.YahooCurrentResponseProcessor;
import org.tenkichannel.weather.api.gateway.yahooweather.YahooQueryRequestProcessor;
import org.tenkichannel.weather.api.gateway.yahooweather.YahooWeatherDataConfig;

@ApplicationScoped
public class YahooWeatherRoute {

    public static final String ROUTE_YAHOO_WEATHER_DATA = "direct:getYahooWeatherData";
    public static final Logger LOGGER = LoggerFactory.getLogger(YahooWeatherRoute.class);

    @Inject
    YahooQueryRequestProcessor yahooProcessor;

    @Inject
    YahooCurrentResponseProcessor yahooResponseProcessor;

    @Inject
    YahooWeatherDataConfig config;

    public RouteBuilder getRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                LOGGER.debug("Configuring route with config {}, queryProcessor {} and responseProcessor {}", config, yahooProcessor, yahooResponseProcessor);
                // @formatter:off
                from(ROUTE_YAHOO_WEATHER_DATA)
                        .log(LoggingLevel.INFO, "Trying to connect to the Yahoo Weather to get current weather data")
                        .process(yahooProcessor)
                        .log(LoggingLevel.INFO, "Headers defined: ${in.headers}")
                        .setBody(constant(null)) //GET request doesn't have a body
                        .doTry()
                            .toF("netty-http:%s%s?ssl=%s", config.getBaseUri(), config.getPath(), config.isSecureProtocol())
                            .convertBodyTo(String.class, "UTF-8")
                            .log(LoggingLevel.DEBUG, "Body response from API call ${body}")
                            .process(yahooResponseProcessor)
                        .doCatch(Exception.class)
                            .log(LoggingLevel.DEBUG, "Body response from API call ${body}")
                            .log(LoggingLevel.ERROR, String.format("Impossible to send message to Yahoo Weather API: ${exchangeProperty[%s]}", Exchange.EXCEPTION_CAUGHT))
                            .convertBodyTo(String.class, "UTF-8")
                        .endDoTry()
                        .log(LoggingLevel.DEBUG, "Body response from API call ${body}");
                // @formatter:on
            }
        };
    }
}
