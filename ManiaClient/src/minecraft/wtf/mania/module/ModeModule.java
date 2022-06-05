package wtf.mania.module;

import wtf.mania.event.EventManager;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.impl.gui.ActiveMods;

public abstract class ModeModule extends Module {
	
	protected ModeObject instance;

	private static ModeObject hudInstance;
	
	public ModeModule(String name, String disc, ModuleCategory category) {
		super(name, disc, category, false);
	}
	
	@Override
	protected void onEnable() {
		EventManager.register(instance);
		instance.onEnable();
	}
	
	@Override
	protected void onDisable() {
		EventManager.unregister(instance);
		instance.onDisable();
	}
	
	@Override
	public void onSetting() {
		if(this.toggled) EventManager.unregister(instance);
		instance = getObject();
		if(this.toggled) EventManager.register(instance);
		this.suffix = getSuffix();
		if(instance != null) instance.onSetting();
		if (ActiveMods.instance.instance != null) ActiveMods.instance.instance.onSetting();
	}
	
	protected abstract ModeObject getObject();
	protected abstract String getSuffix();
	
	protected static abstract class ModeObject {
		
		protected void onEnable() {}
		protected void onDisable() {}
		protected void onSetting() {}
		
	}

}
