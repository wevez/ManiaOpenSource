package com.mania.gui.view.impl;

import com.mania.Mania;
import com.mania.gui.view.View;
import com.mania.management.font.TTFFontRenderer;
import com.mania.util.render.AnimationUtil;
import com.mania.util.render.Render2DUtil;

public class ViewBarButton extends View {
	
	private final TTFFontRenderer fontRenderer;
	private final String name;
	private float animateBar;
	private Runnable clickEvent;
	
	public ViewBarButton(int id, int fontSize, String name) {
		super(id, 0, 0);
		this.fontRenderer = Mania.getFontManager().getFont("light", fontSize);
		this.name = name;
		super.setWidth(this.fontRenderer.getWidth(name));
		this.height = this.fontRenderer.getHeight();
	}
	
	@Override
	public void render(int mouseX, int mouseY) {
		this.fontRenderer.drawString(name, x, y, -1);
		final boolean hovered = super.isOn(mouseX, mouseY);
		final float splitedWidth = super.width / 2;
		animateBar = AnimationUtil.animate(animateBar, hovered ? splitedWidth : 0);
		Render2DUtil.rect(x + splitedWidth - animateBar, y + super.height, animateBar * 2, 1, -1);
	}
	
	@Override
	public void clicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && super.isOn(mouseX, mouseY)) {
			if (this.clickEvent != null) clickEvent.run();
		}
		super.clicked(mouseX, mouseY, mouseButton);
	}
	
	public final void setClickEvent(Runnable event) {
		this.clickEvent = event;
	}

}
