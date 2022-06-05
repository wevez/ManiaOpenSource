package com.mania.module.impl.movement;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventOutWalking;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;

public class SafeWalk extends Module {
	
	public SafeWalk() {
		super("SafeWalk", "Prevents you from falling withoud pressing shift key.", ModuleCategory.Movement, true);
	}
	
	@EventTarget
	public void onOutWalking(EventOutWalking event) {
		event.setSneaking(true);
	}

}
