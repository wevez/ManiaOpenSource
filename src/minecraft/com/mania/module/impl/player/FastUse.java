package com.mania.module.impl.player;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.ModeModule;
import com.mania.module.ModeObject;
import com.mania.module.ModuleCategory;

public class FastUse extends ModeModule {
	
	public FastUse() {
		super("FastUse", "", ModuleCategory.Player, "Type", "Vanilla");
	}
	
	@Override
	protected ModeObject getObject(String mode) {
		switch (mode) {
		case "Vanilla": return new Vanilla();
		}
		return null;
	}
	
	private class Vanilla extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.isPre()) {
				
			}
		}
		
	}

}
