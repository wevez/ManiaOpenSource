package com.mania.module.setting;

import java.util.function.Consumer;

import com.mania.module.Module;

public class ModeSetting extends Setting<String> {
	
	private String value;
	private final String[] modes;
	private int index;
	
	{
		this.index = 0;
	}
	
	public ModeSetting(String name, Module parentModule, String... settings) {
		super(name, parentModule, null, null);
		this.value = settings[0];
		this.modes = settings;
	}
	
	public ModeSetting(String name, Module parentModule, Visibility visibility, String... settings) {
		super(name, parentModule, visibility, null);
		this.value = settings[0];
		this.modes = settings;
	}
	
	public ModeSetting(String name, Module parentModule, Consumer<String> onSetting, String... settings) {
		super(name, parentModule, null, onSetting);
		this.value = settings[0];
		this.modes = settings;
	}
	
	public ModeSetting(String name, Module parentModule, Visibility visibility, Consumer<String> onSetting, String... settings) {
		super(name, parentModule, visibility, onSetting);
		this.value = settings[0];
		this.modes = settings;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String[] getOption() {
		return this.modes;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public boolean is(String value) {
		return this.value.equals(value);
	}
	
	public void setValue(int index) {
		this.index = index;
		this.value = this.modes[index];
		super.onSetting(this.value);
	}
	
	public void setValue(String value) {
		for (int i = 0; i < modes.length; i++) {
			if (this.modes[i].equals(value)) {
				this.value = value;
				this.index = i;
				super.onSetting(this.value);
				return;
			}
		}
	}

}
