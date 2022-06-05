package com.mania.module.impl.misc;

import com.mania.module.ModeModule;
import com.mania.module.ModeObject;
import com.mania.module.ModuleCategory;

public class Crasher extends ModeModule {
	
	public Crasher() {
		super("Crasher", "Just sends some funny packets.", ModuleCategory.Misc, "Mode", "Null");
	}
	
	@Override
	protected ModeObject getObject(String mode) {
		switch (mode) {
		case "Null": return new Null();
		
		}
		return null;
	}
	
	private class Null extends ModeObject {
		
		@Override
		protected void onEnable() {
			// TODO Auto-generated method stub
			super.onEnable();
		}
		
		@Override
		protected void onDisable() {
			// TODO Auto-generated method stub
			super.onDisable();
		}
		
	}

}
