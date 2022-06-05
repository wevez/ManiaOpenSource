package wtf.mania.module.impl.misc;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class PortalGodMode extends Module {
	
	public static Module instance;
	
	public PortalGodMode() {
		// TODO
		super("PortalGodMode", "Makes you invulnerable when you go through a portal", ModuleCategory.Misc, false);
	}

}
