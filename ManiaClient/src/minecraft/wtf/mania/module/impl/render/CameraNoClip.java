package wtf.mania.module.impl.render;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class CameraNoClip extends Module {
	
	public static Module instance;
	
	public CameraNoClip() {
		super("CameraNoClip", "Camera clips through walls in F5", ModuleCategory.Render, false);
		instance = this;
	}

}
