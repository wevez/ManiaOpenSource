package com.mania.module.setting;

import java.util.function.Consumer;

import com.mania.module.Module;

public class BooleanSetting extends Setting<Boolean> {
	
	private boolean value;
	
	public BooleanSetting(String name, Module parentModule, boolean value) {
		super(name, parentModule, null, null);
		this.value = value;
	}
	
	public BooleanSetting(String name, Module parentModule, Visibility visibility, boolean value) {
		super(name, parentModule, visibility, null);
		this.value = value;
	}
	
	public BooleanSetting(String name, Module parentModule, Consumer<Boolean> onSetting, boolean value) {
		super(name, parentModule, null, onSetting);
		this.value = value;
	}
	
	public BooleanSetting(String name, Module parentModule, Visibility visibility, Consumer<Boolean> onSetting, boolean value) {
		super(name, parentModule, visibility, onSetting);
		this.value = value;
	}
	
	public void setValue(boolean value) {
		this.value = !this.value;
		super.onSetting(this.value);
	}
	
	public void switchValue() {
		this.value = !this.value;
		super.onSetting(this.value);
	}
	
	public boolean getValue() {
		return this.value;
	}

}
