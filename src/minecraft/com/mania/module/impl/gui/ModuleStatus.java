package com.mania.module.impl.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventRender2D;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.ColorSetting;
import com.mania.module.setting.ModeSetting;

public class ModuleStatus extends Module {
	
	private final List<Module> boundModuleList;
	
	private final ModeSetting backgroundMode;
	private Runnable backgroundRenderer;
	private final ColorSetting rectangleColor;
	
	public ModuleStatus() {
		super("ModuleStatus", "Displays ", ModuleCategory.Gui, true);
		boundModuleList = new ArrayList<>();
		backgroundMode = new ModeSetting("Background Mode", this, v -> {
			switch (v) {
			case "None" : backgroundRenderer = () -> { }; break;
			case "Rectanble":
				backgroundRenderer = () -> {
					
				};
				break;
			case "Blur":
				backgroundRenderer = () -> {
					
				};
				break;
			}
		}, "None", "Rectangle", "Blur");
		rectangleColor = new ColorSetting("Rectangle", this, () -> backgroundMode.is("Rectangle"), Color.BLACK);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		// background
		backgroundRenderer.run();
		// render module status list
		{
			float offset = 0f;
			for (Module m : boundModuleList) {
				
			}
		}
	}

}
