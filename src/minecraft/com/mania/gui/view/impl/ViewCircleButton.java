package com.mania.gui.view.impl;

import java.awt.Color;

import javax.annotation.Nullable;

import com.mania.Mania;
import com.mania.gui.view.View;
import com.mania.management.font.TTFFontRenderer;
import com.mania.util.render.AnimationUtil;
import com.mania.util.render.ClickUtil;
import com.mania.util.render.Render2DUtil;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ViewCircleButton extends View {
	
	private final String name, icon;
	private final float radius;
	
	private final TTFFontRenderer iconFont, nameFont;
	
	private float hoverAnimation;
	
	private Runnable clickEvent;
	
	public ViewCircleButton(int id, float radius, @Nullable String name, @Nullable String icon) {
		super(id, radius * 2, radius * 2);
		this.name = name;
		this.radius = radius;
		this.icon = icon;
		this.iconFont = Mania.getFontManager().getFont("icon", (int) (this.radius * 2.5));
		this.nameFont = Mania.getFontManager().getFont("light", (int) (this.radius * 0.75));
		this.hoverAnimation = radius;
		this.clickEvent = null;
	}
	
	
	@Override
	public void render(int mouseX, int mouseY) {
		final boolean hovered = ClickUtil.isHovered(x - radius / 2, y - radius / 2, hoverAnimation * 1.5f, hoverAnimation * 1.5f, mouseX, mouseY);
		hoverAnimation = AnimationUtil.animate(hoverAnimation, hovered ? radius * 1.1f : radius);
		Render2DUtil.circle(x, y, hoverAnimation, -1);
		this.iconFont.drawCenteredString(icon, x - radius / 2, y - radius / 2, 0xff000000);
		if (hoverAnimation != radius) {
			// render icon if it is not null
			if (icon != null) {
				
			}
			// render name
			if (name != null) {
				if (radius * 1.1f == hoverAnimation) this.nameFont.drawCenteredString(name, x, y + radius * 1.2f, -1);
				else {
					
					this.nameFont.drawCenteredString(name, x, y + radius * 1.2f, -1);
					
				}
			}
		}
	}
	
	public final void setClickEvent(Runnable event) {
		this.clickEvent = event;
	}
	
	@Override
	public void clicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && clickEvent != null && ClickUtil.isHovered(x - radius / 2, y - radius / 2, hoverAnimation * 1.5f, hoverAnimation * 1.5f, mouseX, mouseY)) {
			clickEvent.run();
			return;
		}
		super.clicked(mouseX, mouseY, mouseButton);
	}

}
