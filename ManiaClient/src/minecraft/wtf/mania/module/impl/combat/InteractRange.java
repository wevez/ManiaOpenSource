package wtf.mania.module.impl.combat;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;

public class InteractRange extends Module {
	
	private static DoubleSetting range;
	
	public InteractRange() {
		super("InteractRange", "Allows you to interact faer away", ModuleCategory.Combat, true);
		settings.add(range = new DoubleSetting("Range", this, 3.98, 3, 8, 0.01));
	}

}
