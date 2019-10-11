package org.tenkichannel.weather.api.gateway.openweather;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tenkichannel.weather.api.gateway.domain.Location;

@ApplicationScoped
public class OpenWeatherQueryRequestProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenWeatherQueryRequestProcessor.class);

    @Inject
    OpenWeatherDataConfig config;

    @Override
    public void process(Exchange exchange) throws Exception {
        final Location location = exchange.getIn().getBody(Location.class);
        LOGGER.debug("Preparing to set input location into exchange: {}", location);

        // @formatter:off
        exchange.getIn().setHeader(
                Exchange.HTTP_QUERY,
                new StringBuilder(this.generateAuthentication())
                        .append("&")
                        .append(this.generateQuery(location)).toString());
        // @formatter:on
        LOGGER.debug("Headers set to {}", exchange.getIn().getHeaders());
    }

    private String generateQuery(final Location location) throws UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder();

        if (location.getCity() != null && !location.getCity().isEmpty()) {
            sb.append("q=")
                    .append(URLEncoder.encode(location.getCity(), "UTF-8"));
            if (location.getCountryCode() != null && !location.getCountryCode().isEmpty()) {
                sb.append(",")
                        .append(location.getCountryCode());
            }
            return sb.toString();
        } else if (location.getCityId() > 0) {
            sb.append("id=").append(location.getCityId());
        } else if (location.getLatitude() != null && location.getLongitude() != null) {
            sb.append("lat=")
                    .append(location.getLatitude())
                    .append("&lon=")
                    .append(location.getLongitude());
        }

        return sb.toString();
    }

    private String generateAuthentication() throws UnsupportedEncodingException {
        return new StringBuilder("APPID=").append(URLEncoder.encode(config.getApiKey(), "UTF-8")).toString();
    }
}
