package com.mania.module.impl.player;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.DoubleSetting;
import com.mania.util.TimerUtil;

public class FastPlace extends Module {
	
	private final TimerUtil timer;
	
	private final DoubleSetting delay;
	
	public FastPlace() {
		super("FastPlace", "Automatically ", ModuleCategory.Player, true);
		this.timer = new TimerUtil();
		delay = new DoubleSetting("Delay", this, 0, 0, 20, 1, "ticks");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!event.isPre()) {
			if (mc.player.ticksExisted % (int) delay.getValue() == 0) {
				
			}
		}
	}

}
