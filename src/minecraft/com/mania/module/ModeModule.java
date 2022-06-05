package com.mania.module;

import com.mania.management.event.EventManager;
import com.mania.module.setting.ModeSetting;

public abstract class ModeModule extends Module {
	
	// i don't like to make a lot of switch words. so i made this LOL.
	
	protected ModeObject instance;
	protected final ModeSetting mode;
	
	public ModeModule(String name, String disc, ModuleCategory category, String modeName, String... option) {
		super(name, disc, category, false);
		this.mode = new ModeSetting(modeName, this, v -> {
			if (super.isEnabled()) EventManager.unregister(instance);
			instance = getObject(v);
			if (super.isEnabled()) EventManager.register(instance);
		}, option);
	}
	
	@Override
	protected void onEnable() {
		EventManager.register(instance);
		System.out.println("A");
		instance.onEnable();
	}
	
	@Override
	protected void onDisable() {
		EventManager.unregister(instance);
		instance.onDisable();
	}
	
	protected abstract ModeObject getObject(String mode);

}
