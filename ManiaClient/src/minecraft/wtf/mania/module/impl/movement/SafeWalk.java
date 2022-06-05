package wtf.mania.module.impl.movement;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class SafeWalk extends Module {
	
	public static Module instance;
	
	public SafeWalk() {
		super("SafeWalk", "Dosen't let you rub off edges", ModuleCategory.Movement, false);
		instance = this;
	}

}
