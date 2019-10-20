package org.tenkichannel.weather.api.gateway.yahooweather.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "wind",
        "atmosphere",
        "astronomy",
        "condition",
        "pubDate"
})
public class CurrentObservation {

    @JsonProperty("wind")
    private Wind wind;
    @JsonProperty("atmosphere")
    private Atmosphere atmosphere;
    @JsonProperty("astronomy")
    private Astronomy astronomy;
    @JsonProperty("condition")
    private Condition condition;
    @JsonProperty("pubDate")
    private Integer pubDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("wind")
    public Wind getWind() {
        return wind;
    }

    @JsonProperty("wind")
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    @JsonProperty("atmosphere")
    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    @JsonProperty("atmosphere")
    public void setAtmosphere(Atmosphere atmosphere) {
        this.atmosphere = atmosphere;
    }

    @JsonProperty("astronomy")
    public Astronomy getAstronomy() {
        return astronomy;
    }

    @JsonProperty("astronomy")
    public void setAstronomy(Astronomy astronomy) {
        this.astronomy = astronomy;
    }

    @JsonProperty("condition")
    public Condition getCondition() {
        return condition;
    }

    @JsonProperty("condition")
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @JsonProperty("pubDate")
    public Integer getPubDate() {
        return pubDate;
    }

    @JsonProperty("pubDate")
    public void setPubDate(Integer pubDate) {
        this.pubDate = pubDate;
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
        return "CurrentObservation{" +
                "wind=" + wind +
                ", atmosphere=" + atmosphere +
                ", astronomy=" + astronomy +
                ", condition=" + condition +
                ", pubDate=" + pubDate +
                '}';
    }
}