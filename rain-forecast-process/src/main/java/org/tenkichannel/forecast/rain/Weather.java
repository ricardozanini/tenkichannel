package org.tenkichannel.forecast.rain;

public class Weather implements java.io.Serializable {

	static final long serialVersionUID = 1L;

	private java.lang.String condition;

	public Weather() {
	}

	public java.lang.String getCondition() {
		return this.condition;
	}

	public void setCondition(java.lang.String condition) {
		this.condition = condition;
	}

	public Weather(java.lang.String condition) {
		this.condition = condition;
	}
	
	@Override
    public String toString() {
        return "Weather [condition=" + condition + "]";
    }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Weather that = (Weather) o;
		if (condition != null
				? !condition.equals(that.condition)
				: that.condition != null)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + (condition != null ? condition.hashCode() : 0);
		return result;
	}

}