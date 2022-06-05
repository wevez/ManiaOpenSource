package com.mania.module.impl.combat;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.util.MoveUtil;

public class KeepSprint extends Module {
	
	public KeepSprint() {
		super("KeepSprint", "", ModuleCategory.Combat, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre() && MoveUtil.isMoving(0.1d)) {
			event.sprinting = true;
		}
	}

}
