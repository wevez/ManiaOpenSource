package wtf.mania.module.impl.world;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class GhostHand extends Module {
	
	public static Module instance;
	
	public GhostHand() {
		super("GhostHand", "Ghost hand", ModuleCategory.World, false);
		instance = this;
	}

}
