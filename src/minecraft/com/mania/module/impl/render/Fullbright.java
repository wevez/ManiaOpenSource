package com.mania.module.impl.render;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.ModeSetting;

public class Fullbright extends Module {
	
	private Runnable updater;
	
	public static boolean toggled, basic, potion;
	
	private final ModeSetting type;
	
	public Fullbright() {
		super("Fullbright", "Makes your ", ModuleCategory.Render, false);
		type = new ModeSetting("Type", this, v -> {
			switch (v) {
			case "Basic":
				basic = true;
				potion = false;
				break;
			case "Potion":
				basic = false;
				potion = true;
				break;
			}
		}, "Basic", "Potion");
	}
}
