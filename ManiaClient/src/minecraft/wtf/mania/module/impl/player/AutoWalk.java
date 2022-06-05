package wtf.mania.module.impl.player;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class AutoWalk extends Module {
	
	public AutoWalk() {
		super("AutoWalk", "Automatically walks forward", ModuleCategory.Player, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			mc.gameSettings.keyBindForward.pressed = true;
		}
	}

}
