package com.mania.module.impl.movement;

import com.mania.management.event.impl.EventUpdate;
import com.mania.management.event.EventTarget;
import com.mania.module.ModeModule;
import com.mania.module.ModeObject;
import com.mania.module.ModuleCategory;

public class Fly extends ModeModule {
	
	public Fly() {
		super("Fly", "", ModuleCategory.Movement, "Type", "Vanilla");
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
