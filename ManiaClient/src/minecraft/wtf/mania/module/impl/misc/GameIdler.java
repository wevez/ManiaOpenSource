package wtf.mania.module.impl.misc;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventKey;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class GameIdler extends Module {
	
	private int prevFPSValue;
	
	private long lastMS;
	
	public GameIdler() {
		super("GameIdler", "Lowers your fps when the game is idle to improve peformances", ModuleCategory.Misc, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre && mc.player.ticksExisted % 20 == 0) {
			if (System.currentTimeMillis() - lastMS > 10000) {
				prevFPSValue = mc.gameSettings.limitFramerate;
				mc.gameSettings.limitFramerate = prevFPSValue;
			}
		}
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		lastMS = System.currentTimeMillis();
		if (mc.gameSettings.limitFramerate == 1) mc.gameSettings.limitFramerate = prevFPSValue;
	}

}
