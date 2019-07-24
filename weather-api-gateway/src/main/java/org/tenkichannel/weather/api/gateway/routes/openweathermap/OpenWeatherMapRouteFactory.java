package org.tenkichannel.weather.api.gateway.routes.openweathermap;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.quarkus.camel.core.runtime.support.FastCamelContext;
import org.apache.camel.CamelContext;
import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.SSLContextParametersAware;
import org.apache.camel.ServiceStatus;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;

@Singleton
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

    ProducerTemplate producerTemplate;

    @PostConstruct
    public void createRoutes() throws Exception {
        if (this.camelContext.getStatus() != ServiceStatus.Started) {
            this.camelContext.start();
        }
        // tls configuration
        if (this.config.isSecureProtocol()) {
            this.configureDefaultSslContextParameters();
        }
        // routes
        this.camelContext.addRoutes(this.createOpenWeatherMapRoute());
        ((FastCamelContext) this.camelContext).reifyRoutes();
        // clients
        this.producerTemplate = this.camelContext.createProducerTemplate();
    }

    @PreDestroy
    public void cleanUp() throws Exception {
        if (this.producerTemplate != null) {
            this.producerTemplate.stop();
        }
    }

    @Produces
    public ProducerTemplate getProducerTemplate() {
        return this.producerTemplate;
    }

    /**
     * Add the default TrustStore to the CamelContext
     * @see <a href="https://livebook.manning.com/#!/book/camel-in-action-second-edition/chapter-14/220">Defining global SSL configuration</a>
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
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
                    .log(LoggingLevel.DEBUG, "Trying to connect to the OpenWeatherMap to get current weather data")
                    .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                    .process(queryRequestProcessor)
                    .log(LoggingLevel.DEBUG, "Headers defined: ${in.headers}")
                    .setBody(constant(null)) //GET request doesn't have a body
                    .doTry()
                        .toF("netty4-http:%s%s?ssl=%s", config.getBaseUri(), OpenWeatherDataConfig.OPEN_WEATHER_MAP_WEATHER_PATH, config.isSecureProtocol())
                        .convertBodyTo(String.class, "UTF-8")
                        .process(currentResponseProcessor)
                    .doCatch(CamelException.class)
                        .log("Impossible to send message to external Weather API: ${body}")
                        .setBody(constant("{ 'error' : 'true' }"))
                    .endDoTry()
                    .log(LoggingLevel.DEBUG, "Body response from API call ${body}");
                // @formatter:on
            }
        };
    }

}
