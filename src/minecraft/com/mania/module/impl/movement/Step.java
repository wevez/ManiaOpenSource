package com.mania.module.impl.movement;

import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.ModeSetting;

public class Step extends Module {
	
	private final ModeSetting type;
	
	public Step() {
		super("Step", "Steps", ModuleCategory.Movement, false);
		type = new ModeSetting("Type", this, "Vanilla", "NCP", "Matrix", "Intave13");
	}

}
