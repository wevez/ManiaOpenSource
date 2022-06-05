package com.mania.module.setting;

import java.util.function.Consumer;

import com.mania.module.Module;
import com.mania.module.impl.gui.ActiveModules;

public abstract class Setting<T> {
	
	/*
	 * The <T> means the cast of method called onSetting
	 * i don't use generic to avoid some lag
	 */
	
	private static final Visibility FAKE_VISIBILITY = () -> true;
	private static final Consumer FAKE_ON_SETTING = v -> {};
	
	private final String name;
	private final Visibility visibility;
	// this consumer is called when the value of setting is changed.
	private final Consumer<T> onSetting;
	
	private final Module parentModule;
	
	public float animation;
	
	protected Setting(String name, Module parentModule, Visibility visibility, Consumer<T> onSetting) {
		super();
		this.onSetting = onSetting == null ? FAKE_ON_SETTING : onSetting;
		this.visibility = visibility == null ? FAKE_VISIBILITY : visibility;
		this.name = name;
		this.parentModule = parentModule;
		this.parentModule.getSettings().add(this);
		/*if (this instanceof BooleanSetting) {
			((BooleanSetting) this).onSetting(((BooleanSetting) this).getValue());
		} else if (this instanceof ColorSetting) {
			
		} else if (this instanceof DoubleSetting) {
			
		} else if (this instanceof ModeSetting) {
			((ModeSetting) this).onSetting(((ModeSetting) this).getValue());
		} else if (this instanceof TextSetting) {
			
		}*/
	}
	
	public void callOnSetting() {
		if (this instanceof BooleanSetting) {
			((BooleanSetting) this).onSetting(((BooleanSetting) this).getValue());
		} else if (this instanceof ColorSetting) {
			
		} else if (this instanceof DoubleSetting) {
			
		} else if (this instanceof ModeSetting) {
			((ModeSetting) this).onSetting(((ModeSetting) this).getValue());
		} else if (this instanceof TextSetting) {
			
		}
	}
	
	protected void onSetting(T value) {
		this.onSetting.accept(value);
		if (value instanceof String) ActiveModules.onSetting();
	}
	
	public final boolean isVisible() {
		return this.visibility.isVisible();
	}
	
	public final String getName() {
		return this.name;
	}
	
	public void init() {
		
	}

}
