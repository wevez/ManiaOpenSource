package wtf.mania.module.impl.player;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class Parkour extends Module {
	
	public Parkour() {
		super("Parkour", "Automatically jumps at the edge of blocks", ModuleCategory.Player, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
	}

}
