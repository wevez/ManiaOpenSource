package wtf.mania.module.impl.render;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;

public class Fullbright extends Module {
	
	private static ModeSetting type;
	
	public Fullbright() {
		super("Fullbright", "Makes you see in the dark", ModuleCategory.Render, false);
		settings.add(type = new ModeSetting("Type", this, "Normal", new String[] {"Normal", "Potion"}));
	}
	
	private float lastGamma;
	
	@Override
	protected void onDisable() {
		mc.gameSettings.gammaSetting = lastGamma;
	}
	
	@Override
	protected void onEnable() {
		lastGamma = mc.gameSettings.gammaSetting;
		mc.gameSettings.gammaSetting = 1E+10f;
	}

}
