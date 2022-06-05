package wtf.mania.module.impl.movement;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;

public class HighJump extends Module {
	
	private static ModeSetting type;
	// vanilla
	private static DoubleSetting vanillaMotion;
	
	public HighJump() {
		super("HighJump", "Makes you jump higher", ModuleCategory.Movement, true);
		settings.add(type = new ModeSetting("Type", this, "Vanilla", new String[] {"Vanilla"}));
		settings.add(vanillaMotion = new DoubleSetting("Motion", this, () -> type.value.equals("Vanilla"), 0.73, 0.42, 5, 0.01));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
	}

}
