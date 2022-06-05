package com.mania.module.impl.gui;

import org.lwjgl.input.Keyboard;

import com.mania.Mania;
import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventKeyboard;
import com.mania.management.event.impl.EventRender2D;
import com.mania.management.font.TTFFontRenderer;
import com.mania.module.ModeModule;
import com.mania.module.ModeObject;
import com.mania.module.ModuleCategory;
import com.mania.util.shader.BlurUtil;

import net.minecraft.client.renderer.GlStateManager;

import static com.mania.util.render.Render2DUtil.*;

public class TabGui extends ModeModule {
	
	private final TTFFontRenderer bold, normal, light;
	
	private int[] indexes;
	private boolean[] expands;
	
	private int categoryIndex, moduleIndex, settingIndex;
	private boolean categoryExpand, moduleEspand;
	
	public TabGui() {
		super("TabGui", "Allows you to controll modules without opening gui.", ModuleCategory.Gui, "Type", "Flux", "Flower", "Sigma");
		this.bold = Mania.getFontManager().getFont("bold", 22);
		this.normal = Mania.getFontManager().getFont("normal", 22);
		this.light = Mania.getFontManager().getFont("light", 24);
		this.indexes = new int[2];
		this.expands = new boolean[3];
	}
	
	@Override
	protected ModeObject getObject(String mode) {
		suffix = mode;
		switch (mode) {
		case "Flower": return new Flower();
		case "Flux": return new Flux();
		case "Sigma": return new Sigma();
		}
		return null;
	}
	
	private class Flower extends ModeObject {
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			rect(5 + 5, 30 + 5, 75, 100, 0xc0000000);
			float offset = 30;
			for (int i = 0, l = ModuleCategory.values().length; i < l; i++) {
				normal.drawString(ModuleCategory.values()[i].name(), 14 + 5, offset + 2 + 5, i == categoryIndex ? -1 : 0xff909090);
				if (categoryIndex == i) rect(5 + 3 + 5, offset + 2 + 5, 3, 15, 0xff00ff00);
				offset += 16;
			}
		}
		
	}
	
	private class Flux extends ModeObject {
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			rect(5 + 5, 30 + 5, 75, 100, 0xc0000000);
			float offset = 30;
			for (int i = 0, l = ModuleCategory.values().length; i < l; i++) {
				bold.drawString(ModuleCategory.values()[i].name(), 14 + 5, offset + 2 + 5, i == categoryIndex ? -1 : 0xff909090);
				if (categoryIndex == i) rect(5 + 3 + 5, offset + 2 + 5, 3, 15, 0xffe09090);
				offset += 16;
			}
			
		}
		
		@EventTarget
		public void onKey(EventKeyboard event) {
			processKey(event.getKeyCode());
		}
		
	}
	
	private class Sigma extends ModeObject {
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			BlurUtil.pre();
			rect(0, 0, 20, 20);
			BlurUtil.post();
		}
		
		@EventTarget
		public void onKey(EventKeyboard event) {
			
		}
		
	}
	
	public void processKey(int keyCode) {
		switch (keyCode) {
		case Keyboard.KEY_LEFT:
		case Keyboard.KEY_RIGHT:
			for (int i = expands.length; i != 0; i++) {
				
			}
			break;
		case Keyboard.KEY_UP:
		case Keyboard.KEY_DOWN:
			
			break;
		case Keyboard.KEY_RETURN:
			
			break;
		}
	}
	
	/*
	 * returns the index which one is clamped and incremented or decremented
	 */
	private int clampIndex(int index, int length, boolean increment) {
		if (increment) return index == length ? 0 : index + 2;
		else return index == 0 ? length : index - 1;
	}

}
