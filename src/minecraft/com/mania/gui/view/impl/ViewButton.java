package com.mania.gui.view.impl;

import javax.annotation.Nullable;

import com.mania.gui.view.View;

import net.minecraft.util.ResourceLocation;

public class ViewButton extends View {
	
	private final ResourceLocation iconLocation;
	private final String name;
	
	private final Runnable animater;
	private Runnable buttonEvent;
	
	public static final int DOWN = 0, UP = 1, RIGHT = 2, LEFT = 3, EXPAND = 4;
	
	public ViewButton(int id, float width, float height, String name, @Nullable ResourceLocation iconLocation, int animationType) {
		super(id, width, height);
		this.name = name;
		this.iconLocation = iconLocation;
		switch (animationType) {
		case DOWN:
			animater = () -> {
				
			};
			break;
		case UP:
			animater = () -> {
				
			};
			break;
		case RIGHT:
			animater = () -> {
				
			};
			break;
		case LEFT:
			animater = () -> {
				
			};
		break;
		case EXPAND:
			animater = () -> {
				
			};
			break;
			default:
				animater = null;
				break;
		}
		this.buttonEvent = null;
	}
	
	@Override
	public void render(int mouseX, int mouseY) {
		animater.run();
	}
	
	public final void setButtonEvent(Runnable r) {
		this.buttonEvent = r;
	}

}
