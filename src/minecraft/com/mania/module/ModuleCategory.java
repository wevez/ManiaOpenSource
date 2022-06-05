package com.mania.module;

import net.minecraft.util.ResourceLocation;

public enum ModuleCategory {
	
	Combat("a"),
	Movement("b"),
	Player("d"),
	Render("c"),
	Gui("J"),
	Misc("g");
	
	private ModuleCategory(String icon) {
		this.ICON = icon;
	}
	
	public final String ICON;

}
