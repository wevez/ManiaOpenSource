package com.mania.gui;

import com.mania.management.keybind.Bindable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiBindable extends GuiScreen implements Bindable {
	
	@Override
	public void keydown() {
		Minecraft.getMinecraft().displayGuiScreen(this);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
