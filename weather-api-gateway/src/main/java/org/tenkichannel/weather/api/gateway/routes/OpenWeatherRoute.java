package org.tenkichannel.weather.api.gateway.routes;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.tenkichannel.weather.api.gateway.openweather.OpenWeatherCurrentResponseProcessor;
import org.tenkichannel.weather.api.gateway.openweather.OpenWeatherDataConfig;
import org.tenkichannel.weather.api.gateway.openweather.OpenWeatherQueryRequestProcessor;

@ApplicationScoped
public class OpenWeatherRoute extends RouteBuilder {

    public static final String ROUTE_OPEN_WEATHER_DATA = "direct:getOpenWeatherData";

    @Inject
    OpenWeatherQueryRequestProcessor queryRequestProcessor;

    @Inject
    OpenWeatherCurrentResponseProcessor currentResponseProcessor;

    @Inject
    OpenWeatherDataConfig config;

    @Override
    public void configure() throws Exception {
        // @formatter:off
        from(ROUTE_OPEN_WEATHER_DATA)
                .log(LoggingLevel.INFO, "Trying to connect to the OpenWeatherMap to get current weather data")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .process(queryRequestProcessor)
                .log(LoggingLevel.INFO, "Headers defined: ${in.headers}")
                .setBody(constant(null)) //GET request doesn't have a body
                .doTry()
                    .toF("netty-http:%s%s?ssl=%s", config.getBaseUri(), OpenWeatherDataConfig.OPEN_WEATHER_MAP_WEATHER_PATH, config.isSecureProtocol())
                    .convertBodyTo(String.class, "UTF-8")
                    .process(currentResponseProcessor)
                .doCatch(Exception.class)
                    .log(LoggingLevel.ERROR, String.format("Impossible to send message to Open Weather API: ${exchangeProperty[%s]}", Exchange.EXCEPTION_CAUGHT))
                    .convertBodyTo(String.class, "UTF-8")
                .endDoTry()
                .log(LoggingLevel.DEBUG, "Body response from API call ${body}");
        // @formatter:on
    }
}
