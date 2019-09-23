package org.tenkichannel.weather.api.gateway.routes.openweathermap;

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
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.SSLContextParametersAware;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.quarkus.core.runtime.CamelRuntime;
import org.apache.camel.quarkus.core.runtime.StartingEvent;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class OpenWeatherMapRouteFactory {

    public static final String ROUTE_CURRENT_WEATHER_DATA = "direct:getCurrentWeatherData";
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenWeatherMapRouteFactory.class);

    @Inject
    CamelRuntime runtime;

    @Inject
    QueryRequestProcessor queryRequestProcessor;

    @Inject
    CurrentResponseProcessor currentResponseProcessor;

    @Inject
    OpenWeatherDataConfig config;

    public void onStarting(@Observes StartingEvent event) throws Exception {
        LOGGER.debug("Starting Camel Runtime");
        runtime.addProperty("starting", "true");
        // tls configuration
        if (this.config.isSecureProtocol()) {
            LOGGER.info("Configuring TLS protocol for {}", config.baseUri);
            this.configureDefaultSslContextParameters();
        }
        LOGGER.debug("Adding route to Camel Context");
        runtime.getContext().addRoutes(createOpenWeatherMapRoute());
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
     *
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

    private RouteBuilder createOpenWeatherMapRoute() {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                // @formatter:off
                from(ROUTE_CURRENT_WEATHER_DATA)
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
                         .log(LoggingLevel.ERROR, String.format("Impossible to send message to external Weather API: ${exchangeProperty[%s]}", Exchange.EXCEPTION_CAUGHT))
                         .convertBodyTo(String.class, "UTF-8")
                    .endDoTry()
                    .log(LoggingLevel.DEBUG, "Body response from API call ${body}");
                // @formatter:on
            }
        };
    }

}
