package wtf.mania.module.impl.player;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class AutoRespawn extends Module {
	
	public AutoRespawn() {
		super("AutoRespawn", "Respawns for you", ModuleCategory.Player, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(event.pre && mc.player.isDead) {
            mc.player.respawnPlayer();
		}
	}

}
