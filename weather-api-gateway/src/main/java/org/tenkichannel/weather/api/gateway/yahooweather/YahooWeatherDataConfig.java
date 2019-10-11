package org.tenkichannel.weather.api.gateway.yahooweather;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.tenkichannel.weather.api.gateway.DataConfig;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class YahooWeatherDataConfig implements DataConfig {

    @ConfigProperty(name = YahooWeatherDataProperties.YAHOO_WEATHER_APP_ID)
    String yahooAppID;

    @ConfigProperty(name = YahooWeatherDataProperties.YAHOO_WEATHER_APP_CONSUMER_KEY)
    String consumerKey;

    @ConfigProperty(name = YahooWeatherDataProperties.YAHOO_WEATHER_APP_CONSUMER_SECRET)
    String consumerSecret;

    @ConfigProperty(name = YahooWeatherDataProperties.YAHOO_WEATHER_BASE_URI)
    String baseUri;

    public YahooWeatherDataConfig() {
    }

    public String getBaseUri() {
        return baseUri;
    }

    public String getPath() {
        return "/forecastrss";
    }

    public String getYahooAppID() {
        return yahooAppID;
    }

    public void setYahooAppID(String yahooAppID) {
        this.yahooAppID = yahooAppID;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    @Override
    public boolean isSecureProtocol() {
        if (baseUri != null) {
            return baseUri.contains("https");
        }
        return  false;
    }
}
