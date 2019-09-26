package org.tenkichannel.weather.api.gateway.domain;

import java.io.Serializable;
import java.util.Objects;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Domain model for requesting weather data.
 */
@RegisterForReflection
public class Location implements Serializable {

    private static final long serialVersionUID = 2072056895286744369L;

    private String city;
    private String countryCode;
    private Integer cityId;
    private Double latitude;
    private Double longitude;

    public Location() {
        this.cityId = 0;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public Location(int cityId) {
        this.cityId = cityId;
    }

    public Location(String cityQuery) {
        if (cityQuery != null && cityQuery.indexOf(",") > 0) {
            this.city = cityQuery.substring(0, cityQuery.indexOf(","));
            this.countryCode = cityQuery.substring(cityQuery.indexOf(",") + 1, cityQuery.length());
        } else {
            this.city = cityQuery;
        }
    }

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, cityId, countryCode, latitude, longitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location other = (Location) obj;
        return Objects.equals(city, other.city) && Objects.equals(cityId, other.cityId) && Objects.equals(countryCode, other.countryCode) && Objects.equals(latitude, other.latitude) && Objects.equals(longitude,
                                                                                                                                                                                                        other.longitude);
    }

    @Override
    public String toString() {
        return "Location [city=" + city + ", countryCode=" + countryCode + ", cityId=" + cityId + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    }

}
