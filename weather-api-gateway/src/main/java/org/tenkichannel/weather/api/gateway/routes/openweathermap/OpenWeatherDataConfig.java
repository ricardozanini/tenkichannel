package org.tenkichannel.weather.api.gateway.routes.openweathermap;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class OpenWeatherDataConfig {

    public static final String OPEN_WEATHER_MAP_WEATHER_PATH = "/weather";

    @ConfigProperty(name = OpenWeatherDataProperties.OPEN_WEATHER_MAP_BASE_URI)
    private String baseUri;

    @ConfigProperty(name = OpenWeatherDataProperties.OPEN_WEATHER_MAP_API_KEY)
    private String apiKey;

    public OpenWeatherDataConfig() {}

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isSecureProtocol() {
        return "https".contains(baseUri);
    }

}
