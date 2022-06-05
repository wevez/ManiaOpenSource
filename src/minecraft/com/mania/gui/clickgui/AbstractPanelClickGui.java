package com.mania.gui.clickgui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mania.gui.GuiBindable;
import com.mania.management.keybind.Bindable;
import com.mania.module.ModuleCategory;

import net.minecraft.client.gui.GuiScreen;

public class AbstractPanelClickGui <T extends AbstractPanel> extends GuiBindable {
	
	protected final List<T> panels;

	public AbstractPanelClickGui() {
		this.panels = new ArrayList<>();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		panels.forEach(p -> p.drawPanel(mouseX, mouseY));
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		panels.forEach(p -> p.onClicked(mouseX, mouseY, mouseButton));
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		panels.forEach(p -> p.mouseReleased(mouseX, mouseY, state));
	}
	
	@Override
	public void initGui() {
		super.initGui();
	}

}
