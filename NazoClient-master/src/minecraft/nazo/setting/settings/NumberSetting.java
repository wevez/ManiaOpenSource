package nazo.setting.settings;

import nazo.setting.Setting;

public class NumberSetting extends Setting{
	
	public double value, minimum, maximum, increment;
	
	public NumberSetting(String name, double value, double minimum, double maximum, double increment) {
		this.name = name;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.increment = increment;
	}
	
	public void setValue(double value) {
		double presecion = 1 / increment;
		this.value = Math.round(Math.max(minimum, Math.min(maximum, value)) * presecion) / presecion;
	}
	
	public void increment(boolean positive) {
		setValue(this.value + (positive ? 1 : -1) * increment);
	}

}
