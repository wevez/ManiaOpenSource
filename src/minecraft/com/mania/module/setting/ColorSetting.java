package com.mania.module.setting;

import java.awt.Color;

import com.mania.module.Module;

public class ColorSetting extends Setting {
	
	// max value is 1
	private final float[] hsb = new float[3];
	private float alpha;
	// value
	private int hex;
	
	public ColorSetting(String name, Module parentModule, Color color) {
		super(name, parentModule, null, null);
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), this.hsb);
	}
	
	public ColorSetting(String name, Module parentModule, Visibility visibility, Color color) {
		super(name, parentModule, visibility, null);
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), this.hsb);
	}
	
	public void setHSB(float hue, float saturation, float brightness) {
		this.hsb[0] = hue;
		this.hsb[1] = saturation;
		this.hsb[2] = brightness;
		this.setValue();
	}
	
	private void setValue() {
		final Color hsbColor = new Color(Color.HSBtoRGB(this.hsb[0], this.hsb[1], this.hsb[2]));
		this.hex = new Color(hsbColor.getRed(), hsbColor.getGreen(), hsbColor.getBlue(), this.alpha).getRGB();
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
		this.setValue();
	}
	
	public int getHex() {
		return this.hex;
	}
}
