package com.mania.gui.view.impl;

import com.mania.gui.view.View;
import com.mania.management.font.TTFFontRenderer;

import net.minecraft.util.math.MathHelper;

import static com.mania.util.render.Render2DUtil.*;

public class ViewTextInput extends View {
	
	private final TTFFontRenderer fontRenderer;
	protected boolean focused;
	private int tabtick;
	private boolean tabFlag, dragging;
	private int currentTextIndex;
	private final int focusedTextColor, textColor;
	private String currentText;
	private final String name;
	private int cursorStart, cursorEnd;
	
	public ViewTextInput(int id, float width, float height, TTFFontRenderer fontRenderer, String name, String text, int focusedTextColor, int textColor) {
		super(id, width, height);
		this.fontRenderer = fontRenderer;
		this.focusedTextColor = focusedTextColor;
		this.textColor = textColor;
		this.name = name;
		this.currentText = text;
	}
	
	@Override
	public void render(int mouseX, int mouseY) {
		if (focused) {
			// update tab
			++tabtick;
			if (tabtick % 20 == 0) {
				this.tabFlag = !this.tabFlag;
			}
			
		}
		// render tab
		
		// render current text view
		this.fontRenderer.drawString(this.currentText.isEmpty() ? this.name : this.currentText, this.x + 1, this.y + 1, this.focused ? this.focusedTextColor : this.textColor);
	}
	
	@Override
	public void keyTyped(int keyCode) {
		if (this.focused) {
			switch (keyCode) {
			
			}
		}
		super.keyTyped(keyCode);
	}
	
	@Override
	public void release() {
		this.dragging = false;
		super.release();
	}
	
	@Override
	public void clicked(int mouseX, int mouseY, int mouseButton) {
		if (isOn(mouseX, mouseY)) {
			this.focused = true;
			dragging = true;
		} else {
			this.focused = false;
		}
		super.clicked(mouseX, mouseY, mouseButton);
	}
	
	private void setCursorEnd(int value) {
		this.cursorEnd = MathHelper.clamp(value, 0, this.currentText.length());
	}
	
	private void setStartPosition(int value) {
		
	}

}
