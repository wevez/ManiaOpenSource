package com.mania.module.impl.render;

import java.awt.Color;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventRender3D;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.ColorSetting;
import com.mania.module.setting.ModeSetting;

public class Cosmetics extends Module {
	
	// china hat
	private final BooleanSetting chinaHat;
	// wing
	private final BooleanSetting wing;
	private final ColorSetting wingColor;
	// cloak
	private final BooleanSetting cloak;
	private final ModeSetting cloakMode;
	
	public Cosmetics() {
		super("Cosmetics", "Renders ", ModuleCategory.Render, true);
		// china hat
		chinaHat = new BooleanSetting("China Hat", this, false);
		
		// wing
		wing = new BooleanSetting("Wing", this, false);
		wingColor = new ColorSetting("Wing Color", this, Color.black);
		// cloak
		cloak = new BooleanSetting("Cloak", this, false);
		cloakMode = new ModeSetting("Cloak Mode", this, cloak::getValue, v -> {
			switch (v) {
			case "Mania":
				
				break;
			case "Exhibition":
				
				break;
			}
		}, "Mania", "Exhibition");
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		// china hat
		if (chinaHat.getValue()) {
			
		}
		
	}

}
