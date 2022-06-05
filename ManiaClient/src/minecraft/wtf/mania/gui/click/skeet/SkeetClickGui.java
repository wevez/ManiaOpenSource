package wtf.mania.gui.click.skeet;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Mouse;

import wtf.mania.Mania;
import wtf.mania.gui.click.GuiMoveable;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.*;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.MiscUtils;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Stencil;

public class SkeetClickGui extends GuiMoveable {
	
	// size
	private int width = 400, height = 250;
	private boolean sizing;
	// all alpha
	private int alpha;
	// category
	private ModuleCategory category;
	// modules
	private Setting setting;
	private List<Module> modules;
	private int[] moduleScrolls;
	private int moduleScroll;
	private float animatedModuleScroll;
	private float[] animatedModuleScrolls;
	
	public SkeetClickGui() {
		category = ModuleCategory.Combat;
	}
	
	@Override
	public void initGui() {
		alpha = 5;
		setting = null;
		modules = Mania.instance.moduleManager.getModulesBycCategory(category);
		moduleScrolls = new int[modules.size()];
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int scroll = Mouse.getDWheel() / 10;
		// alpha
		if (alpha < 255) {
			alpha += 25;
		}
		
		// sizing
		if (sizing) {
			width = mouseX - x;
			height = mouseY - y;
		}
		
		int lineColor = new Color(25, 25, 25, alpha).getRGB(), blackColor = new Color(0, 0, 0, alpha).getRGB(), whiteColor = new Color(255, 255, 255, alpha).getRGB(), greenColor = new Color(100, 175, 50, alpha).getRGB();
		// base
		Render2DUtils.drawRect(x, y, x+width, y+height, lineColor);
		Render2DUtils.drawRect(x+2.5f, y+2.5f, x+width-2.5f, y+height-2.5f, blackColor);
		Render2DUtils.drawRect(x+2.5f, y+7.5f, x+width-2.5f, y+10, lineColor);
		Render2DUtils.drawRect(x+45, y+7.5f, x+47.5f, y+height-2.5f, lineColor);
		
		// scroll
		if (scroll > 0) {
			moduleScroll += scroll;
			if (moduleScroll > 0) moduleScroll = 0;
		} else if (scroll != 0) {
			moduleScroll += scroll;
			if (moduleScroll < - (modules.size() -3) * 50) moduleScroll = (modules.size()-3) * - 50;
		}
		
		int offset = 0;
		
		// category
		int categoryColor = new Color(175, 175, 175, alpha).getRGB();
		for (ModuleCategory c : ModuleCategory.values()) {
			ColorUtils.glColor(c == category ? whiteColor : categoryColor);
			Render2DUtils.drawImage(c.icon, x+15, y+20+offset, 16, 16);
			offset += 28;
		}
		
		// modules
		offset = (int) (animatedModuleScroll = AnimationUtils.animate(animatedModuleScroll, moduleScroll, 0.75f));
		int xOffset = 0;
		Stencil.write(false);
		Render2DUtils.drawRect(x+2.5f, y+10, x+width-2.5f, y+height-2.5f, blackColor);
		Stencil.erase(true);
		for (Module m : modules) {
			if (xOffset + 125 > width) {
				width = 400;
				xOffset = 0;
				offset += 100;
			}
			// base
			Render2DUtils.drawRect(x + 57.5f + xOffset, y + 23f + offset, x + 57.5f + xOffset + 165, y + 22.5f + offset + 95, lineColor);
			Render2DUtils.drawRect(x + 57.5f + 2.5f + xOffset, y + 2.5f + 22.5f + offset, x + 57.5f + xOffset + 165 - 2.5f, y - 2.5f + 22.5f + offset + 95, blackColor);
			// toggle
			Render2DUtils.drawRect(x + 62.5f + xOffset, y + 20 + offset, x + 71 + xOffset, y + 28.5f + offset, lineColor);
			if (m.toggled) Render2DUtils.drawRect(x + 63.5f + xOffset, y + 21 + offset, x + 70 + xOffset, y + 27.5f + offset, greenColor);
			Mania.instance.fontManager.medium9.drawString(m.name, x + 72.5f + xOffset, y + 20 + offset, whiteColor);
			Mania.instance.fontManager.light7.drawString(m.disc, x + 62.5f + xOffset, y + 30 + offset, whiteColor);
			
			// setting
			int settingOffset = 0;
			float settingX = x + 67.5f + xOffset, settingY = y + 40 + offset;
			for (Setting s : m.settings) {
				if (!(boolean) s.visibility.get()) continue;
				Render2DUtils.drawRect(x + 63 + xOffset, settingY + settingOffset + 2.5f, x + 65.5f + xOffset, settingY + settingOffset + 5, lineColor);
				Mania.instance.fontManager.light7.drawString(s.name, settingX, settingY + settingOffset, whiteColor);
				if (s instanceof DoubleSetting) {
					DoubleSetting debu = (DoubleSetting) s;
					if (setting == debu) MiscUtils.setDoubleValue(debu, settingX + 75.5f, settingY + settingOffset + 1.5f, 75, mouseX, mouseY);
					Render2DUtils.drawRect(settingX + 75, settingY + settingOffset + 1, settingX + 150, settingY + settingOffset + 8, lineColor);
					float percent = (float) MiscUtils.getPercent(debu);
					Render2DUtils.drawRect(settingX + 75.5f, settingY + settingOffset + 1.5f, settingX + 74.5f + percent * 75, settingY + settingOffset + 7.5f, greenColor);
					Mania.instance.fontManager.light4.drawStringWithShadow(String.format("%s %s", debu.value.toString(), debu.unit), settingX + 72 + percent * 75, settingY + settingOffset + 3, whiteColor);
					settingOffset += 10;
				} else if (s instanceof ModeSetting) {
					ModeSetting busu = (ModeSetting) s;
					Render2DUtils.drawRect(settingX + 75, settingY + settingOffset, settingX + 150, settingY + settingOffset + 8, lineColor);
					Mania.instance.fontManager.light7.drawString(busu.value, settingX + 82.5f, settingY + settingOffset, whiteColor);
					if (setting == s) {
						
					} else {
						Render2DUtils.drawTriangle(settingX + 77, settingY + settingOffset + 2, settingX + 79, settingY + settingOffset + 6, settingX + 81, settingY + settingOffset + 2, whiteColor);
					}
					settingOffset += 10;
				} else if (s instanceof ColorSetting) {
					((ColorSetting) s).drawPicker(settingX, settingY + settingOffset + 7, 1, blackColor);
					settingOffset += 60;
				} else if (s instanceof BooleanSetting) {
					Render2DUtils.drawRect(settingX + 75, settingY + settingOffset + 1, settingX + 81, settingY + settingOffset + 7, lineColor);
					if ((boolean) s.value) {
						Render2DUtils.drawRect(settingX + 76, settingY + settingOffset + 2, settingX + 80, settingY + settingOffset + 6, greenColor);
					}
					settingOffset += 10;
				} else if (s instanceof TextSetting) {
					settingOffset += 10;
				}
			}
			Render2DUtils.drawRect(x + 57.5f + xOffset, y + 22.5f + offset + 92.5f, x + 57.5f + xOffset + 165, y + 22.5f + offset + 95, lineColor);
			Render2DUtils.drawRect(x + 57.5f + xOffset, y + 22.5f + offset + 95, x + 57.5f + xOffset + 165, mc.displayHeight, blackColor);
			xOffset += 170;
		}
		Stencil.dispose();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		// category
		int offset = 0;
		for (ModuleCategory c : ModuleCategory.values()) {
			if (ClickUtils.isMouseHovering(x + 15, y+20+offset, 16, 16, mouseX, mouseY)) {
				category = c;
				modules = Mania.instance.moduleManager.getModulesBycCategory(c);
				moduleScrolls = new int[modules.size()];
				moduleScroll = 0;
				return;
			}
			offset += 28;
		}
		
		// modules
		offset = (int) (animatedModuleScroll = AnimationUtils.animate(animatedModuleScroll, moduleScroll));
		int xOffset = 0;
		for (Module m : modules) {
			if (xOffset + 125 > width) {
				width = 400;
				xOffset = 0;
				offset += 100;
			}
			if (ClickUtils.isMouseHovering2(x + 62.5f + xOffset, y + 20 + offset, x + 71 + xOffset, y + 28.5f + offset, mouseX, mouseY)) {
				m.toggle();
				return;
			}
			
			// setting
			int settingOffset = 0;
			float settingX = x + 67.5f + xOffset, settingY = y + 40 + offset;
			for (Setting s : m.settings) {
				if (!(boolean) s.visibility.get()) continue;
				if (s instanceof DoubleSetting) {
					DoubleSetting debu = (DoubleSetting) s;
					if (ClickUtils.isMouseHovering2(settingX + 75, settingY + settingOffset + 1, settingX + 150, settingY + settingOffset + 8, mouseX, mouseY)) {
						setting = debu;
						return;
					}
					settingOffset += 10;
				} else if (s instanceof ModeSetting) {
					if (ClickUtils.isMouseHovering2(settingX + 75, settingY + settingOffset, settingX + 150, settingY + settingOffset + 8, mouseX, mouseY)) {
						setting = s;
						return;
					}
					if (s == setting) {
						
						return;
					}
					settingOffset += 10;
				} else if (s instanceof ColorSetting) {
					((ColorSetting) s).onClicked(mouseX, mouseY);
					settingOffset += 60;
				} else if (s instanceof BooleanSetting) {
					if (ClickUtils.isMouseHovering2(settingX + 75, settingY + settingOffset + 1, settingX + 81, settingY + settingOffset + 7, mouseX, mouseY)) {
						s.value = !((BooleanSetting) s).value;
						return;
					}
					settingOffset += 10;
				} else if (s instanceof TextSetting) {
					
					settingOffset += 10;
				}
				if (settingOffset > 95) break;
			}
			xOffset += 170;
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		setting = null;
		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	protected boolean isOn(int mouseX, int mouseY) {
		return ClickUtils.isMouseHovering(super.x, y, width, height, mouseX, mouseY);
	}

}
