package org.tenkichannel.forecast.rain;

public class Location implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    private java.lang.String city;
    private java.lang.String countryCode;
    private java.lang.Double latitude;
    private java.lang.Double longitude;

    public Location() {}

    public java.lang.String getCity() {
        return this.city;
    }

    public void setCity(java.lang.String city) {
        this.city = city;
    }

    public java.lang.String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(java.lang.String countryCode) {
        this.countryCode = countryCode;
    }

    public java.lang.Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(java.lang.Double latitude) {
        this.latitude = latitude;
    }

    public java.lang.Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(java.lang.Double longitude) {
        this.longitude = longitude;
    }

    public Location(java.lang.String city, java.lang.String countryCode,
                    java.lang.Double latitude, java.lang.Double longitude) {
        this.city = city;
        this.countryCode = countryCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location [city=" + city + ", countryCode=" + countryCode + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Location that = (Location) o;
        if (city != null ? !city.equals(that.city) : that.city != null)
            return false;
        if (countryCode != null
                ? !countryCode.equals(that.countryCode)
                : that.countryCode != null)
            return false;
        if (latitude != null
                ? !latitude.equals(that.latitude)
                : that.latitude != null)
            return false;
        if (longitude != null
                ? !longitude.equals(that.longitude)
                : that.longitude != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }

}
