package com.mania.gui.clickgui;

import java.util.List;

import com.mania.Mania;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;

public abstract class AbstractPanel {
	
	protected final ModuleCategory category;
	protected final List<Module> modules;
	public int x, y;
	private int lastX, lastY;
	protected boolean dragging;
	
	public AbstractPanel(ModuleCategory category, int x, int y) {
		this.x = x;
		this.y = y;
		this.category = category;
		this.modules = Mania.getModuleManager().getModules(category);
	}
	
	public void drawPanel(int mouseX, int mouseY) {
		if (this.dragging) {
            this.x = mouseX + this.lastX;
            this.y = mouseY + this.lastY;
        }
	}
	
	public void onClicked(int mouseX, int mouseY, int mouseButton) {
		if (isOn(mouseX, mouseY) && mouseButton == 0 && !this.dragging) {
			this.lastX = x - mouseX;
	        this.lastY = y - mouseY;
	        this.dragging = true;
		}
	}
	
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (this.dragging) this.dragging = false;
	}
	
	protected abstract boolean isOn(int mouseX, int mouseY);

}
