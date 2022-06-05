package com.mania.module.impl.gui;

import com.mania.management.event.impl.EventRender2D;
import com.mania.management.event.EventTarget;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;

public class DebugHUD extends Module {
	
	public DebugHUD() {
		super("DebugHUD", "Displays something for debugging.", ModuleCategory.Gui, true);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		
	}

}
