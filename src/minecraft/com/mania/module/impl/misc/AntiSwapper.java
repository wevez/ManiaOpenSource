package com.mania.module.impl.misc;

import com.mania.management.event.impl.EventPacket;
import com.mania.management.event.impl.EventUpdate;
import com.mania.management.event.EventTarget;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;

public class AntiSwapper extends Module {
	
	private boolean swapped;
	
	public AntiSwapper() {
		super("AntiSwapper", "Automatically escapes from swapper trap.", ModuleCategory.Misc, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			if (swapped) {
				
			}
		}
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.isOutgoing()) {
			
		} else {
			
		}
	}

}
