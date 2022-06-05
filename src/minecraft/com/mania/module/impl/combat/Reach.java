package com.mania.module.impl.combat;

import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.DoubleSetting;

public class Reach extends Module {
	
	private final DoubleSetting range;
	
	public Reach() {
		super("Reach", "Extends your reach.", ModuleCategory.Combat, true);
		range = new DoubleSetting("Range", this, 3, 6, 0, 0.1, "m");
	}

}
