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
public class WeatherGatewayRouteFactory {

    public static final String WEATHER_ROUTE = "direct:getWeatherData";
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherGatewayRouteFactory.class);

    @Inject
    CamelRuntime runtime;

    @Inject
    OpenWeatherRoute openWeather;

    @Inject
    YahooWeatherRoute yahooWeather;

    @Inject
    OpenWeatherDataConfig openWeatherDataConfig;

    @Inject
    YahooWeatherDataConfig yahooWeatherDataConfig;

    public void onStarting(@Observes StartingEvent event) throws Exception {
        LOGGER.debug("Starting Camel Runtime");
        runtime.addProperty("starting", "true");
        // tls configuration
        if (this.openWeatherDataConfig.isSecureProtocol() || this.yahooWeatherDataConfig.isSecureProtocol()) {
            LOGGER.info("Configuring TLS protocol for {} and {}", openWeatherDataConfig.getBaseUri(), yahooWeatherDataConfig.getBaseUri());
            this.configureDefaultSslContextParameters();
        }
        LOGGER.debug("Adding route to Camel Context");
        runtime.getContext().addRoutes(getWeatherRoute());
        runtime.getContext().addRoutes(openWeather);
        runtime.getContext().addRoutes(yahooWeather);
    }

    void onStart(@Observes StartupEvent ev) throws Exception {
        runtime.addProperty("started", "true");
        LOGGER.info("Camel Runtime started");
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("Camel Runtime stopped");
    }

    /**
     * Add the default TrustStore to the CamelContext
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @see <a href="https://livebook.manning.com/#!/book/camel-in-action-second-edition/chapter-14/220">Defining global SSL configuration</a>
     */
    private void configureDefaultSslContextParameters() throws NoSuchAlgorithmException, KeyStoreException {
        final SSLContextParameters sslContextParameters = new SSLContextParameters();
        final TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore) null);
        X509TrustManager defaultTm = null;
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                defaultTm = (X509TrustManager) tm;
                break;
            }
        }
        trustManagersParameters.setTrustManager(defaultTm);
        sslContextParameters.setTrustManagers(trustManagersParameters);
        this.runtime.getContext().setSSLContextParameters(sslContextParameters);
        LOGGER.debug("SSL Context parameters set to Camel Context: {}", sslContextParameters);
        // define our components to also use the default one.
        ((SSLContextParametersAware) this.runtime.getContext().getComponent("netty-http")).setUseGlobalSslContextParameters(true);
    }

    private RouteBuilder getWeatherRoute() {
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
