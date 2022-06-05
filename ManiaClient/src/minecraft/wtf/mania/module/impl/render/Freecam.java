package wtf.mania.module.impl.render;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;

public class Freecam extends Module {
	
	private static DoubleSetting speed;
	
	public Freecam() {
		super("Freecam", "Move client side but not server side", ModuleCategory.Render, true);
		settings.add(speed = new DoubleSetting("Speed", this, 4, 1, 10, 0.1));
	}

}
