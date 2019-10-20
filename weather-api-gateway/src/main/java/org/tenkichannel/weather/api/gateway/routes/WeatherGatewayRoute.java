package org.tenkichannel.weather.api.gateway.routes;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.camel.SSLContextParametersAware;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.quarkus.core.runtime.CamelRuntime;
import org.apache.camel.quarkus.core.runtime.StartingEvent;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tenkichannel.weather.api.gateway.openweather.OpenWeatherDataConfig;
import org.tenkichannel.weather.api.gateway.yahooweather.YahooWeatherDataConfig;

@ApplicationScoped
public class WeatherGatewayRoute  {

    public static final String WEATHER_ROUTE = "direct:getWeatherData";

    @Inject
    OpenWeatherRoute openWeather;

    @Inject
    YahooWeatherRoute yahooWeather;

    public RouteBuilder getRoute() {
        return new RouteBuilder() {
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
        };
    }
}
