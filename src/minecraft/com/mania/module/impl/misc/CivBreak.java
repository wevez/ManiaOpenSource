package com.mania.module.impl.misc;

import java.util.function.Consumer;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.util.BlockData;

public class CivBreak extends Module {
	
	private final DoubleSetting range;
	private final BooleanSetting throughWalls;
	private final ModeSetting mode;
	
	private Consumer<BlockData> breaker;
	
	public CivBreak() {
		super("CivBreak", "Breaks nexus faster.", ModuleCategory.Misc, true);
		range = new DoubleSetting("Range", this, 3, 0, 6, 0.1, "m");
		throughWalls = new BooleanSetting("Through Walls", this, true);
		mode = new ModeSetting("Mode", this, v -> {
			switch (v) {
			case "Legit":
				breaker = b -> {
					
				};
				break;
			}
		}, "Legit");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			
		} else {
			
		}
	}

}
