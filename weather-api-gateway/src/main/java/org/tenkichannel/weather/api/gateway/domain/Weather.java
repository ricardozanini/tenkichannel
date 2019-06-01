package org.tenkichannel.weather.api.gateway.domain;

import java.io.Serializable;
import java.util.Objects;

public class Weather implements Serializable {

    private static final long serialVersionUID = -4564845012061865521L;
    private String condition;

    public Weather() {

    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Weather [condition=" + condition + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Weather other = (Weather) obj;
        return Objects.equals(condition, other.condition);
    }

}
