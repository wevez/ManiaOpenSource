package wtf.mania.module.impl.world;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;

public class AntiCactus extends Module {
	
	private static BooleanSetting above;
	
	public AntiCactus() {
		super("AntiCactus", "Prevent you from taking damage from cactus", ModuleCategory.World, true);
		settings.add(above = new BooleanSetting("Above", this, true));
	}

}
