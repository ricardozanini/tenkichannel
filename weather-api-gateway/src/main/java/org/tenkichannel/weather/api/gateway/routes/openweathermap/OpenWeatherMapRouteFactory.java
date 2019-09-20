package org.tenkichannel.weather.api.gateway.routes.openweathermap;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.SSLContextParametersAware;
import org.apache.camel.ServiceStatus;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.quarkus.core.runtime.support.FastCamelContext;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;

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
public class OpenWeatherMapRouteFactory {

    public static final String ROUTE_CURRENT_WEATHER_DATA = "direct:getCurrentWeatherData";

    @Inject
    QueryRequestProcessor queryRequestProcessor;

    @Inject
    CurrentResponseProcessor currentResponseProcessor;

    @Inject
    OpenWeatherDataConfig config;

    @Inject
    CamelContext camelContext;

    @Inject
    ProducerTemplate producerTemplate;


    void onStart(@Observes StartupEvent ev) throws Exception {
        if (this.camelContext.getStatus() != ServiceStatus.Started) {
            this.camelContext.start();
        }
        // tls configuration
        if (this.config.isSecureProtocol()) {
            this.configureDefaultSslContextParameters();
        }
        // routes
        this.camelContext.addRoutes(this.createOpenWeatherMapRoute());
        ((FastCamelContext) this.camelContext).doInit();

        // clients
        this.producerTemplate = this.camelContext.createProducerTemplate();
    }

    void onStop(@Observes ShutdownEvent ev) {
        if (this.producerTemplate != null) {
            this.producerTemplate.stop();
        }
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
        this.camelContext.setSSLContextParameters(sslContextParameters);
        // define our components to also use the default one.
        ((SSLContextParametersAware) this.camelContext.getComponent("netty4-http")).setUseGlobalSslContextParameters(true);
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
                        .toF("netty4-http:%s%s?ssl=%s", config.getBaseUri(), OpenWeatherDataConfig.OPEN_WEATHER_MAP_WEATHER_PATH, config.isSecureProtocol())
                        .convertBodyTo(String.class, "UTF-8")
                        .process(currentResponseProcessor)
                        .doCatch(Exception.class)
                        .log("Impossible to send message to external Weather API: ${body}")
                        //.setBody(constant("{ 'error' : 'true' }"))
                        .endDoTry()
                        .log(LoggingLevel.DEBUG, "Body response from API call ${body}");
                // @formatter:on
            }
        };
    }

}
