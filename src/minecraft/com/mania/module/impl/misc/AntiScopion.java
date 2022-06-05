package com.mania.module.impl.misc;

import com.mania.management.event.impl.EventUpdate;
import com.mania.management.event.EventTarget;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;

public class AntiScopion extends Module {
	
	public AntiScopion() {
		super("AntiScopion", "", ModuleCategory.Misc, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			
		}
	}

}
