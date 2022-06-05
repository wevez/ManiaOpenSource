package wtf.mania.module.impl.render;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class NoHurtCam extends Module {
	
	public static Module instance;
	
	public NoHurtCam() {
		super("NoHurtCam", "Disables the hurt animation", ModuleCategory.Render, false);
		instance = this;
	}

}
