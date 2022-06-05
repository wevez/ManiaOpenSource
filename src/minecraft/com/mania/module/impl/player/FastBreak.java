package com.mania.module.impl.player;

import com.mania.management.event.impl.EventPacket;
import com.mania.management.event.impl.EventUpdate;
import com.mania.management.event.EventTarget;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;

public class FastBreak extends Module {
	
	private final ModeSetting type;
	private final DoubleSetting speed;
	
	public FastBreak() {
		super("FastBreak", "Makes your digging speed faster.", ModuleCategory.Player, true);
		type = new ModeSetting("Type", this, v -> {
			switch (v) {
			case "Custom":
				
				break;
			case "Packet":
				
				break;
			}
		}, "Custom", "Packet");
		speed = new DoubleSetting("Speed", this, 7, 0, 10, 1, "ticks");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			
		}
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.isOutgoing()) {
			
		}
	}

}
