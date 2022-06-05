package com.mania.module.impl.misc;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventPacket;
import com.mania.management.event.impl.EventRender3D;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.DoubleSetting;

public class Freecam extends Module {
	
	private final DoubleSetting moveSpeed;
	private final BooleanSetting showPath;
	
	public Freecam() {
		super("Freecam", "", ModuleCategory.Render, true);
		moveSpeed = new DoubleSetting("Move Speed", this, 1, 0, 10, 1, "bps");
		showPath = new BooleanSetting("Show Path", this, false);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			
		}
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		if (showPath.getValue()) {
			
		}
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.isOutgoing()) {
			
		}
	}

}
