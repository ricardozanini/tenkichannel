package org.tenkichannel.weather.api.gateway.yahooweather;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URI;

/**
 * This class is meant to replace the netty-http component that, for some reason is having problems to
 * call the Yahoo API. Let's wait some more time to Camel Quarkus extension to stabilize, meanwhile let's use this
 * custom processor.
 * See: https://github.com/ricardozanini/tenkichannel/issues/10
 */
@ApplicationScoped
public class YahooHttpClientRequestProcessor  implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(YahooHttpClientRequestProcessor.class);

    @Inject
    YahooWeatherDataConfig config;

    @Override
    public void process(Exchange exchange) throws Exception {
        final String content = Request.Get(URI.create(config.getBaseUri() + config.getPath() + "?" + exchange.getIn().getHeader(Exchange.HTTP_QUERY)))
                .addHeader("X-Yahoo-App-Id", exchange.getIn().getHeader("X-Yahoo-App-Id").toString())
                .addHeader("Authorization", exchange.getIn().getHeader("Authorization").toString())
                .addHeader(Exchange.CONTENT_TYPE, exchange.getIn().getHeader(Exchange.CONTENT_TYPE).toString())
                .execute()
                .returnContent().asString();
        LOGGER.debug("HttpClient processor return: \n {}", content);
        exchange.getIn().setBody(content);
    }

}
