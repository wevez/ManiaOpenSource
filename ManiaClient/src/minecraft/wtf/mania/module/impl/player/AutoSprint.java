package wtf.mania.module.impl.player;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.impl.movement.BlockFlyRecode;
import wtf.mania.module.impl.movement.Strafe;

public class AutoSprint extends Module {
	
	public AutoSprint() {
		super("AutoSprint", "Sprints for you", ModuleCategory.Player, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(event.pre) {
			if(canSprint()) mc.gameSettings.keyBindSprint.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
			else {
				mc.player.setSprinting(false);
				mc.gameSettings.keyBindSprint.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
			}
		}
	}
	
	private static boolean canSprint() {
		return BlockFlyRecode.canSprint() && Strafe.canSprint();
	}

}
