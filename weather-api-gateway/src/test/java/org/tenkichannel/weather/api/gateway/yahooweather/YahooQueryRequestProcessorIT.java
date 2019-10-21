package org.tenkichannel.weather.api.gateway.yahooweather;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Request;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.tenkichannel.weather.api.gateway.domain.Location;

import javax.inject.Inject;

import java.io.IOException;
import java.net.URI;


import static org.junit.jupiter.api.Assertions.*;

/**
 * This test is meant to verify the Yahoo Service and should not run under normal circumstances.
 * To run it, just replace the Yahoo Service credentials above and remove the @Disabled annotation.
 */
public class YahooQueryRequestProcessorIT {

    @Disabled
    @Test
    public void testDirectCallYahooService() throws IOException, InterruptedException {
        final YahooQueryRequestProcessor requestProcessor = new YahooQueryRequestProcessor();
        final YahooWeatherDataConfig config = new YahooWeatherDataConfig();
        final Location location = new Location("recife");

        config.baseUri = "https://weather-ydn-yql.media.yahoo.com";
        config.consumerKey = "";
        config.yahooAppID = "";
        config.consumerSecret = "";
        requestProcessor.config = config;

        final String query = requestProcessor.generateQuery(location);
        final String authorization = requestProcessor.prepareAuthorization(location.getCity());

        final String content = Request.Get(URI.create(config.baseUri + config.getPath() + "?" + query))
                .addHeader("Authorization", authorization)
                .addHeader("X-Yahoo-App-Id", config.yahooAppID)
                .addHeader("Content-Type", "application/json")
                .execute()
                .returnContent().asString();
        assertNotNull(content);
    }
}