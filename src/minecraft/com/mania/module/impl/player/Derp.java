package com.mania.module.impl.player;

import com.mania.management.event.impl.EventUpdate;
import com.mania.management.event.EventTarget;
import com.mania.management.event.Priority;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;

public class Derp extends Module {
	
	public Derp() {
		super("Derp", "", ModuleCategory.Player, true);
	}
	
	@EventTarget(Priority.FIFTH)
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			
		}
	}

}
