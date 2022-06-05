package wtf.mania.module.impl.movement;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;

public class BoatFly extends Module {
	
	private static DoubleSetting speed;
	
	public BoatFly() {
		super("BoatFly", "Fly with a boat", ModuleCategory.Movement, true);
		settings.add(speed = new DoubleSetting("Speed", this, 4, 0.28, 10, 0.01));
	}

}
