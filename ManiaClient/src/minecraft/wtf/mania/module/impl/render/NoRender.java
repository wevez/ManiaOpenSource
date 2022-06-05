package wtf.mania.module.impl.render;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;

public class NoRender extends Module {
	
	public static Module instance;
	
	public static BooleanSetting armor, weather, fog;
	
	public NoRender() {
		super("NoRender", "No Render", ModuleCategory.Render, false);
		settings.add(armor = new BooleanSetting("Armor", this, false));
		settings.add(weather = new BooleanSetting("Weather", this, false));
		settings.add(fog = new BooleanSetting("Fog", this, false));
		instance = this;
	}

}
