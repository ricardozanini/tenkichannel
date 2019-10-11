package org.tenkichannel.weather.api.gateway.yahooweather;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tenkichannel.weather.api.gateway.domain.Location;

import static org.apache.camel.language.constant.ConstantLanguage.constant;

@ApplicationScoped
public class YahooQueryRequestProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    YahooWeatherDataConfig config;

    @Override
    public void process(Exchange exchange) {
        final Location location = exchange.getIn().getBody(Location.class);
        LOGGER.debug("Preparing to set input location into exchange: {}", location);

        String locationNormalized = normalize(location.getCity());

        exchange.getIn().setHeader(Exchange.HTTP_METHOD, constant("GET"));
        exchange.getIn().setHeader(Exchange.HTTP_QUERY, generateQuery(location));
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getIn().setHeader("X-Yahoo-App-Id", config.getYahooAppID());
        exchange.getIn().setHeader("Authorization", prepareAuthorization(locationNormalized));

        LOGGER.debug("Headers set to {}", exchange.getIn().getHeaders());
    }

    private String generateQuery(final Location location) {
        final StringBuilder sb = new StringBuilder();

        if (null != location.getCity() || !location.getCity().isEmpty()) {
            String normalizedLocation = normalize(location.getCity());
            sb.append("location=").append(normalizedLocation);
        } else if (location.getCityId() > 0) {
            sb.append("woeid=").append(location.getCityId());
        } else if (null != location.getLatitude() && null != location.getLongitude()) {
            sb.append("lat=")
                    .append(location.getLatitude())
                    .append("&lon=")
                    .append(location.getLongitude());
        }
        sb.append("&format=json");
        return sb.toString();
    }

    /**
     * Normalize the parameter and remove accents
     * @param param
     * @return encoded param
     */
    private String normalize(String param) {
        String normalized = Normalizer.normalize(param, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        normalized = normalized.replace("\"", "").replace(" ", "+");
        try {
            normalized = URLEncoder.encode(normalized, StandardCharsets.UTF_8.displayName());
            LOGGER.debug(String.format("Parameter to encode: [%s], Encoded: [%s]", param, normalized));
        } catch (final Exception e) {
            LOGGER.debug("Failed to encode " + param);
        }
        return normalized;
    }

    /**
     * Prepare the Oauth Authorization Header
     * @param location
     * @return Authorization Oauth 1.0
     */
    public String prepareAuthorization(String location) {

        long timestamp = new Date().getTime() / 1000;
        byte[] nonce = new byte[32];
        Random rand = new Random();
        rand.nextBytes(nonce);
        String oauthNonce = new String(nonce).replaceAll("\\W", "");

        List<String> parameters = new ArrayList<>();
        parameters.add("oauth_consumer_key=" + config.getConsumerKey());
        parameters.add("oauth_nonce=" + oauthNonce);
        parameters.add("oauth_signature_method=HMAC-SHA1");
        parameters.add("oauth_timestamp=" + timestamp);
        parameters.add("oauth_version=1.0");
        // Make sure value is encoded
        parameters.add("location=" + location);
        parameters.add("format=json");
        Collections.sort(parameters);

        StringBuffer parametersList = new StringBuffer();
        for (int i = 0; i < parameters.size(); i++) {
            parametersList.append(((i > 0) ? "&" : "") + parameters.get(i));
        }

        String signatureString = null;
        try {
            signatureString = "GET&" +
                    URLEncoder.encode(config.getBaseUri() + "" + config.getPath(), "UTF-8") + "&" +
                    URLEncoder.encode(parametersList.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String signature = null;
        try {
            SecretKeySpec signingKey = new SecretKeySpec((config.getConsumerSecret() + "&").getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHMAC = mac.doFinal(signatureString.getBytes());
            Base64.Encoder encoder = Base64.getEncoder();
            signature = encoder.encodeToString(rawHMAC);
        } catch (Exception e) {
            System.err.println("Unable to append signature");
            System.exit(0);
        }

        return "OAuth " +
                "oauth_consumer_key=\"" + config.getConsumerKey() + "\", " +
                "oauth_nonce=\"" + oauthNonce + "\", " +
                "oauth_timestamp=\"" + timestamp + "\", " +
                "oauth_signature_method=\"HMAC-SHA1\", " +
                "oauth_signature=\"" + signature + "\", " +
                "oauth_version=\"1.0\"";
    }
}
