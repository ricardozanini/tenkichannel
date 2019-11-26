package org.tenkichannel.weather.api.gateway.routes;

import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class WeatherGatewayRoute extends RouteBuilder {

    public static final String WEATHER_ROUTE = "direct:getWeatherData";

    @Inject
    OpenWeatherRoute openWeather;

    @Inject
    YahooWeatherRoute yahooWeather;

    @Override
    public void configure() throws Exception {
        // @formatter:off
        from(WEATHER_ROUTE)
                .loadBalance()
                .roundRobin()
                .to(openWeather.ROUTE_OPEN_WEATHER_DATA)
                .to(yahooWeather.ROUTE_YAHOO_WEATHER_DATA);
        // @formatter:on
    }
}
