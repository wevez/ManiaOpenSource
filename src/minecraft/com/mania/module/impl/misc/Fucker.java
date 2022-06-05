package com.mania.module.impl.misc;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.DoubleSetting;

public class Fucker extends Module {
	
	private final DoubleSetting range;
	private final BooleanSetting rayTrace;
	
	public Fucker() {
		super("Fucker", "Automatically breaks .", ModuleCategory.Misc, true);
		range = new DoubleSetting("Range", this, 4, 6, 0, 0.1, "m");
		rayTrace = new BooleanSetting("RayTrace", this, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			
		}
	}

}
