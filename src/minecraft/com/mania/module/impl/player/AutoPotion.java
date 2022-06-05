package com.mania.module.impl.player;

import com.mania.management.event.impl.EventUpdate;
import com.mania.management.event.EventTarget;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.ModeSetting;
import com.mania.module.setting.Visibility;

public class AutoPotion extends Module {
	
	private final ModeSetting throwTiming;
	
	private Visibility canThrow;
	
	public AutoPotion() {
		super("AutoPotion", "Automatically throws potions for you.", ModuleCategory.Player, true);
		throwTiming = new ModeSetting("Throw Timing", this, v -> {
			switch (v) {
			case "Floor":
				canThrow = () -> {
					return mc.player.onGround;
				};
				break;
			case "Jump":
				canThrow = () -> {
					return false;
				};
			}
		}, "Floor", "Jump");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			if (canThrow.isVisible()) {
				
			}
		}
	}

}
