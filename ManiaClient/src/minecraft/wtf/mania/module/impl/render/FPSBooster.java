package wtf.mania.module.impl.render;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class FPSBooster extends Module {
	
	public static Module instance;
	
	public FPSBooster() {
		super("FPSBooster", "Disables Armor Stand and particle rendering", ModuleCategory.Render, false);
	}

}
