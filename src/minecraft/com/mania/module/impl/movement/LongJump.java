package com.mania.module.impl.movement;

import com.mania.management.event.impl.EventUpdate;
import com.mania.management.event.EventTarget;
import com.mania.module.ModeModule;
import com.mania.module.ModeObject;
import com.mania.module.ModuleCategory;

public class LongJump extends ModeModule {
	
	public LongJump() {
		super("LongJump", "", ModuleCategory.Movement, "Type", "NCP", "Matrix");
	}
	
	@Override
	protected ModeObject getObject(String mode) {
		switch (mode) {
		case "Matrix": return new Matrix();
		case "NCP": return new NCP();
		}
		return null;
	}
	
	private class Matrix extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			
		}
		
	}
	
	private class NCP extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			
		}
		
	}

}
