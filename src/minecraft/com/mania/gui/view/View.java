package com.mania.gui.view;

import com.mania.util.render.ClickUtil;

public abstract class View {
	
	protected final int id;
	protected float x, y, width, height;
	protected boolean visible;
	
	protected View(int id, float width, float height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public final void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public final void setWidth(float width) {
		this.width = width;
	}
	
	public final void setHeight(float height) {
		this.height = height;
	}
	
	public void setVisible(boolean flag) {
		this.visible = flag;
	}
	
	// returns whether mouse is on the own view
	protected final boolean isOn(int mouseX, int mouseY) {
		return ClickUtil.isHovered(this.x, this.y, this.width, this.height, mouseX, mouseY);
	}
	
	public final int getId() {
		return this.id;
	}
	
	/*
	 * Functions which one is called from GuiScreenWithView.java
	 */
	public abstract void render(int mouseX, int mouseY);
	
	public void keyTyped(int keyCode) {
		
	}
	
	public void clicked(int mouseX, int mouseY, int mouseButton) {
		
	}
	
	public void release() {
		
	}

}
