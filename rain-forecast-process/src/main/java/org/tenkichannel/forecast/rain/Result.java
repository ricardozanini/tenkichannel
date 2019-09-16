package org.tenkichannel.forecast.rain;


public class Result implements java.io.Serializable {

	static final long serialVersionUID = 1L;

	private java.lang.Boolean isRain;

	public Result() {
	    this.isRain = false;
	}

	public java.lang.Boolean getIsRain() {
		return this.isRain;
	}

	public void setIsRain(java.lang.Boolean isRain) {
		this.isRain = isRain;
	}

	public Result(java.lang.Boolean isRain) {
		this.isRain = isRain;
	}
	
	@Override
	public String toString() {
	    return isRain.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Result that = (Result) o;
		if (isRain != null
				? !isRain.equals(that.isRain)
				: that.isRain != null)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result
				+ (isRain != null ? isRain.hashCode() : 0);
		return result;
	}

}