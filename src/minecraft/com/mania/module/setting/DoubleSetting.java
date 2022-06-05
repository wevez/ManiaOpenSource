package com.mania.module.setting;

import java.util.function.Consumer;

import com.mania.module.Module;

public class DoubleSetting extends Setting<Double> {
	
	private final double min, max, inc;
	private final String unit;
	
	private double value;
	
	public DoubleSetting(String name, Module parentModule, double value, double min, double max, double inc, String unit) {
		super(name, parentModule, null, null);
		this.value = value;
		this.min = min;
		this.max = max;
		this.inc = inc;
		this.unit = unit;
	}
	
	public DoubleSetting(String name, Module parentModule, Visibility visibility, double value, double min, double max, double inc, String unit) {
		super(name, parentModule, visibility, null);
		this.value = value;
		this.min = min;
		this.max = max;
		this.inc = inc;
		this.unit = unit;
	}
	
	/*public DoubleSetting(String name, Module parentModule, Consumer<Double> onSetting, double value, double min, double max, double inc, String unit) {
		super(name, parentModule, null, onSetting);
		this.value = value;
		this.min = min;
		this.max = max;
		this.inc = inc;
		this.unit = unit;
	}
	
	public DoubleSetting(String name, Module parentModule, Visibility visibility, Consumer<Double> onSetting, double value, double min, double max, double inc, String unit) {
		super(name, parentModule, visibility, onSetting);
		this.value = value;
		this.min = min;
		this.max = max;
		this.inc = inc;
		this.unit = unit;
	}*/
	
	public double getValue() {
		return this.value;
	}
	
	public double getPercent() {
		return (this.value - this.min) / (this.max - this.min);
	}
	
	public void setValue(double value) {
		this.value = value;
		super.onSetting(this.value);
	}
	
	public String getUnit() {
		return this.unit;
	}

}
