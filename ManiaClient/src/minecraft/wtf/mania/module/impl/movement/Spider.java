package wtf.mania.module.impl.movement;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;

public class Spider extends Module {
	
	private static ModeSetting type, jumpMode;
	// vanilla
	private static DoubleSetting vanillaMotion;
	// jump
	private static BooleanSetting autoJump;
	
	public Spider() {
		super("Spider", "Climp walls like spiders", ModuleCategory.Movement, true);
		settings.add(type = new ModeSetting("Type", this, "Vanilla", new String[] {"Vanilla", "Jump"}));
		settings.add(vanillaMotion = new DoubleSetting("Motion", this, () -> type.is("Vanilla"), 0.35, 0.2, 1, 0.01));
		settings.add(jumpMode = new ModeSetting("Mode", this, () -> type.is("Jump"), "Spartan", new String[] {"Spartan"}));
		settings.add(autoJump = new BooleanSetting("AutoJump", this, true));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		switch (type.value) {
		case "Vanilla":
			if (event.pre && mc.player.isCollidedHorizontally) {
				mc.player.motionY = vanillaMotion.value;
			}
			break;
		case "Spartan":
			
			break;
		}
	}

}
