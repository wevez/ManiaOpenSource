package wtf.mania.module.data;

import java.util.function.Supplier;

import wtf.mania.module.Module;

public class DoubleSetting extends Setting<Double> {
	
	public final double min, max, increment;
	public final String unit;
	private static String fake = "";
	
	public DoubleSetting(String name, Module parentModule, Supplier<Boolean> visibility, double value, double min, double max, double increment, String unit) {
		super(name, parentModule, visibility, value);
		this.min = min;
		this.max = max;
		this.increment = increment;
		this.unit = unit;
	}
	
	public DoubleSetting(String name, Module parentModule, double value, double min, double max, double increment, String unit) {
		super(name, parentModule, value);
		this.min = min;
		this.max = max;
		this.increment = increment;
		this.unit = unit;
	}

	public DoubleSetting(String name, Module parentModule, Supplier<Boolean> visibility, double value, double min, double max, double increment) {
		super(name, parentModule, visibility, value);
		this.min = min;
		this.max = max;
		this.increment = increment;
		this.unit = fake;
	}
	
	public DoubleSetting(String name, Module parentModule, double value, double min, double max, double increment) {
		super(name, parentModule, value);
		this.min = min;
		this.max = max;
		this.increment = increment;
		this.unit = fake;
	}
	
	public void setValue(double value) {
		double presecion = 1 / increment;
		this.value = Math.round(Math.max(min, Math.min(max, value)) * presecion) / presecion;
	}
	
	public void increment(boolean positive) {
		setValue(this.value.doubleValue() + (positive ? 1 : -1) * increment);
	}

}
