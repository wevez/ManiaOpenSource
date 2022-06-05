package wtf.mania.module.impl.render;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class LowFire extends Module {
	
	public static LowFire instance;

	public LowFire() {
		super("LowFire", "Makes the fire transparent when you're burning", ModuleCategory.Render, false);
		instance = this;
	}

}
