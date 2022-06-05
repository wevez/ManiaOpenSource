package wtf.mania.module.impl.player;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;

public class Derp extends Module {
	
	private static ModeSetting rotationMode;
	
	public Derp() {
		super("Derp", "Spazzes around", ModuleCategory.Player, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(event.pre) {
			
		}
	}

}
