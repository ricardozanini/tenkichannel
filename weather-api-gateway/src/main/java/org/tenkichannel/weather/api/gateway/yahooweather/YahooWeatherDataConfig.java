package org.tenkichannel.weather.api.gateway.yahooweather;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class YahooWeatherDataConfig {

    @ConfigProperty(name = YahooWeatherDataProperties.YAHOO_WEATHER_APP_ID)
    String yahooAppID;

    @ConfigProperty(name = YahooWeatherDataProperties.YAHOO_WEATHER_APP_CONSUMER_KEY)
    String consumerKey;

    @ConfigProperty(name = YahooWeatherDataProperties.YAHOO_WEATHER_APP_CONSUMER_SECRET)
    String consumerSecret;

    public YahooWeatherDataConfig() {
    }

    public String getBaseURl() {
        return "https://weather-ydn-yql.media.yahoo.com";
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
}
