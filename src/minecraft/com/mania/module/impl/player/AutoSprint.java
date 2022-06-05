package com.mania.module.impl.player;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.Visibility;
import com.mania.util.PlayerUtil;

public class AutoSprint extends Module {
	
	private final BooleanSetting omni;
	
	private Visibility canSprint;
	
	public AutoSprint() {
		super("AutoSprint", "Automatically", ModuleCategory.Player, true);
		omni = new BooleanSetting("Omni", this, v -> {
			if (v) canSprint = () -> true;
			else canSprint = PlayerUtil::canSprint;
		}, false);
	}
	
	@EventTarget
	public void onUpdat(EventUpdate event) {
		if (event.isPre() && mc.player.ticksExisted % 2 == 0 && canSprint.isVisible()) { 
			mc.player.setSprinting(true);
		}
	}

}
