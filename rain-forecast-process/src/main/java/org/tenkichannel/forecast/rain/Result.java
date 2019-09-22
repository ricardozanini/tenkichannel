package org.tenkichannel.forecast.rain;


public class Result implements java.io.Serializable {

	static final long serialVersionUID = 1L;

	private java.lang.Boolean rain;

	public Result() {
	    this.rain = false;
	}

	public java.lang.Boolean isRain() {
		return this.rain;
	}

	public void setRain(java.lang.Boolean isRain) {
		this.rain = isRain;
	}

	public Result(java.lang.Boolean isRain) {
		this.rain = isRain;
	}
	
	@Override
	public String toString() {
	    return rain.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Result that = (Result) o;
		if (rain != null
				? !rain.equals(that.rain)
				: that.rain != null)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result
				+ (rain != null ? rain.hashCode() : 0);
		return result;
	}

}