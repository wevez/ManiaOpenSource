package com.mania.module.impl.player;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.ModeSetting;

public class NoWeb extends Module {
	
	private final ModeSetting type;
	
	private Runnable escaper;
	
	public NoWeb() {
		super("NoWeb", "", ModuleCategory.Player, true);
		type = new ModeSetting("Type", this, v -> {
			switch (v) {
			case "Vanilla":
				escaper = () -> {
					mc.player.setInWeb(false);
				};
				break;
			case "AAC":
				escaper = () -> {
					
				};
				break;
			case "Intave":
				escaper = () -> {
					
				};
				break;
			}
		}, "Vanilla", "AAC", "Intave");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			
		}
	}

}
