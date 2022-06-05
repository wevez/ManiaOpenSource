package com.mania.module.setting;

import com.mania.module.Module;

import net.minecraft.client.gui.GuiScreen;

public class TextSetting extends Setting {
	
	private String value;
	private int textIndex;
	public boolean focused;
	private boolean allSelecting;
	
	public TextSetting(String name, Module parentModule, String value) {
		super(name, parentModule, null, null);
		this.value = value;
	}
	
	public TextSetting(String name, Module parentModule, Visibility visibility, String value) {
		super(name, parentModule, visibility, null);
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public void onKey(int keyCode) {
		if (focused) return;
		if (GuiScreen.isKeyComboCtrlA(keyCode)) {
			this.allSelecting = true;
		} else if (GuiScreen.isKeyComboCtrlC(keyCode)) {
			if (this.allSelecting) GuiScreen.setClipboardString(value);
		} else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
			this.setValue(GuiScreen.getClipboardString());
		} else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
			if (allSelecting) GuiScreen.setClipboardString(value);
			this.setValue("");
		} else {
			switch (keyCode) {
			case 14:
				if (GuiScreen.isCtrlKeyDown()) {
					
				} else {
					
				}
				break;
				
			}
		}
	}
	
	public final boolean isAllSelecting() {
		return this.allSelecting;
	}

}
