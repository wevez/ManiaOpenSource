package com.mania.module.impl.render;

import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.ModeSetting;

public class CustomItemRenderer extends Module {
	
	// block hit
	private final ModeSetting blockHitMode;
	public static Runnable blockHitRenderer;
	
	public CustomItemRenderer() {
		super("CustomItemRenderer", "", ModuleCategory.Render, false);
		blockHitMode = new ModeSetting("Block Hit Mode", this, v -> {
			switch (v) {
			
			}
		}, "None", "1.7", "Exhibition");
	}

}
