package org.tenkichannel.weather.api.gateway.yahooweather.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "location",
        "current_observation",
        "forecasts"
})
public class YahooQueryResponse {

    @JsonProperty("location")
    private Location location;
    @JsonProperty("current_observation")
    private CurrentObservation current_observation;
    @JsonProperty("forecasts")
    private List<Forecast> forecasts = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonProperty("current_observation")
    public CurrentObservation getCurrent_observation() {
        return current_observation;
    }

    @JsonProperty("current_observation")
    public void setCurrent_observation(CurrentObservation current_observation) {
        this.current_observation = current_observation;
    }

    @JsonProperty("forecasts")
    public List<Forecast> getForecasts() {
        return forecasts;
    }

    @JsonProperty("forecasts")
    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "YahooQueryResponse{" +
                "location=" + location +
                ", current_observation=" + current_observation +
                ", forecasts=" + forecasts +
                '}';
    }
}