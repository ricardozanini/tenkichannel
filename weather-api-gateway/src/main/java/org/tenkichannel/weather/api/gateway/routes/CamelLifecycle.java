package org.tenkichannel.weather.api.gateway.routes;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.camel.CamelContext;
import org.apache.camel.SSLContextParametersAware;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tenkichannel.weather.api.gateway.openweather.OpenWeatherDataConfig;
import org.tenkichannel.weather.api.gateway.yahooweather.YahooWeatherDataConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class CamelLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherGatewayRoute.class);

    @Inject
    CamelContext context;

    @Inject
    OpenWeatherDataConfig openWeatherDataConfig;

    @Inject
    YahooWeatherDataConfig yahooWeatherDataConfig;

    public void onStarting(@Observes StartupEvent event) throws Exception {
        LOGGER.debug("Configuration set for Open Weather: {}", openWeatherDataConfig);
        LOGGER.debug("Configuration set for Yahoo Weather: {}", yahooWeatherDataConfig);

        LOGGER.debug("Starting Camel Runtime");
        // tls configuration
        if (this.openWeatherDataConfig.isSecureProtocol() || this.yahooWeatherDataConfig.isSecureProtocol()) {
            LOGGER.info("Configuring TLS protocol for {} and {}", openWeatherDataConfig.getBaseUri(), yahooWeatherDataConfig.getBaseUri());
            this.configureDefaultSslContextParameters();
        }
        LOGGER.debug("Adding route to Camel Context");
    }

    void onStart(@Observes StartupEvent ev) {
        //runtime.addProperty("started", "true");
        LOGGER.info("Camel Runtime started");
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("Camel Runtime stopped");
    }

    /**
     * Add the default TrustStore to the CamelContext
     *
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
        this.context.setSSLContextParameters(sslContextParameters);
        LOGGER.debug("SSL Context parameters set to Camel Context: {}", sslContextParameters);
        // define our components to also use the default one.
        ((SSLContextParametersAware) this.context.getComponent("netty-http")).setUseGlobalSslContextParameters(true);
    }

}

