package com.mania.module;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.mania.MCHook;
import com.mania.Mania;
import com.mania.management.event.EventManager;
import com.mania.management.keybind.Bindable;
import com.mania.management.keybind.Keybind;
import com.mania.module.impl.gui.Notifications;
import com.mania.module.setting.Setting;
import com.mania.util.SoundUtil;

public abstract class Module implements MCHook, Bindable {
	
	private final String name, description;
	protected String suffix;
	private final ModuleCategory category;
	
	private final boolean eventable;
	
	private boolean toggled;
	
	private final List<Setting<?>> settings;
	
	protected Module(String name, String description, ModuleCategory category, boolean eventable) {
		this.name = name;
		this.description = description;
		this.settings = new ArrayList<>();
		this.eventable = eventable;
		this.category = category;
	}
	
	public final void toggle() {
		this.toggled = !this.toggled;
		if (this.toggled) {
			this.onEnable();
			if (eventable) EventManager.register(this);
		} else {
			this.onDisable();
			if (eventable) EventManager.unregister(this);
		}
		Notifications.addToggle(new Notifications.ToggleNotification(this.toggled ? Notifications.ToggleNotificationType.ENABLE : Notifications.ToggleNotificationType.DISABLE, String.format("%s %s.", this.toggled ? "Enabled" : "Disabled", this.name), 60));
		// TODO custom sound
		SoundUtil.playSound(this.toggled ? SoundUtil.ENABLE : SoundUtil.DISABLE);
	}
	
	public final boolean isEnabled() {
		return this.toggled;
	}
	
	public final boolean isDisalbed() {
		return !this.toggled;
	}
	
	protected void onEnable() {
		
	}
	
	protected void onDisable() {
		
	}
	
	public final boolean isSame(ModuleCategory category) {
		return this.category == category;
	}
	
	@Override
	public void keydown() {
		this.toggle();
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final String getDescription() {
		return this.description;
	}
	
	public final List<Setting<?>> getSettings() {
		return this.settings;
	}
	
	public final String getSuffix() {
		return this.suffix;
	}

}
