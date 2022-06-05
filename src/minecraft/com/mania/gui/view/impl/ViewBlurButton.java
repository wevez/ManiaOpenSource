package com.mania.gui.view.impl;

import com.mania.gui.view.View;

public class ViewBlurButton extends View {
	
	private Runnable buttonEvent;
	
	public ViewBlurButton(int id, float width, float height) {
		super(id, width, height);
		this.buttonEvent = null;
	}
	
	@Override
	public void render(int mouseX, int mouseY) {
		
	}
	
	@Override
	public void clicked(int mouseX, int mouseY, int mouseButton) {
		if (this.buttonEvent != null && isOn(mouseX, mouseY)) {
			this.buttonEvent.run();
		}
		super.clicked(mouseX, mouseY, mouseButton);
	}
	
	public final void setButtonEvent(Runnable buttonEvent) {
		this.buttonEvent = buttonEvent;
	}

}
