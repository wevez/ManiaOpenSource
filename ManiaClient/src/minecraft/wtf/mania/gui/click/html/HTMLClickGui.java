package wtf.mania.gui.click.html;

import java.awt.Color;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.ibm.icu.util.ULocale.Category;

import net.minecraft.client.gui.Gui;
import wtf.mania.Mania;
import wtf.mania.gui.ScrollBall;
import wtf.mania.gui.box.ModeSettingBox;
import wtf.mania.gui.box.TextBox;
import wtf.mania.gui.click.GuiMoveable;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.data.Setting;
import wtf.mania.module.data.TextSetting;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Stencil;

public class HTMLClickGui extends GuiMoveable {
	
	private int categoryIndex;
	private boolean expanded;
	private float animatedCategoryExpand, animatedCategory, animatedExpand;
	private List<Module> modules;
	private TextBox moduleSearch;
	private Module focusedModule;
	private List<ModeSettingBox> modeBoxes;
	private ScrollBall moduleScroll, settingScroll;
	
	public HTMLClickGui() {
		setCategory(ModuleCategory.Combat);
		moduleSearch = new TextBox("Module Search", 0, 0, 100, 20, "", Mania.instance.fontManager.light10);
		modeBoxes = new LinkedList<>();
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// base
		Render2DUtils.drawSmoothRect(x, y, x+400, y+250, -1);
		Render2DUtils.drawSmoothRect(x, y+32, x+195, y+250, 0xfff9f9f9);
		Render2DUtils.drawSmoothRect(x, y, x+400, y+32, 0xffededed);
		Mania.instance.fontManager.bold20.drawString(Mania.name, x+5, y+5, 0xff303030);
		// category
		if(ClickUtils.isMouseHovering(x, y+32, animatedCategory, 223, mouseX, mouseY)) animatedCategory = AnimationUtils.animate(animatedCategory, 100);
		else animatedCategory = AnimationUtils.animate(animatedCategory, 30);
		Stencil.write(false);
		Render2DUtils.drawSmoothRect(x, y+32, x+animatedCategory, y+250, 0xff1f1f25);
		Stencil.erase(true);
		Render2DUtils.drawSmoothRect(x, y, x+100, y+250, 0xfff3f3f3);
		int categoryOffset = y + 35;
		for(ModuleCategory c : ModuleCategory.values()) {
			if(ClickUtils.isMouseHovering(x, categoryOffset, animatedCategory, 20, mouseX, mouseY)) Render2DUtils.drawSmoothRect(x, categoryOffset, x+animatedCategory, categoryOffset+19, 0x50303030);
			ColorUtils.glColor(0xff303030);
			mc.getTextureManager().bindTexture(c.icon);
			Gui.drawModalRectWithCustomSizedTexture(x+6, categoryOffset, 0, 0, 16, 16, 16, 16);
			Mania.instance.fontManager.light12.drawString(c.toString(), x+30, categoryOffset+2, 0xff303030);
			categoryOffset += 20;
		}
		Stencil.dispose();
		// modules
		Stencil.write(false);
		Render2DUtils.drawRect(x+animatedCategory, y, x+200, y+250, 0xff1f1f25);
		Stencil.erase(true);
		int moduleOffset = y+40;
		for(Module m : modules) {
			Mania.instance.fontManager.light12.drawString(m.name, x+45, moduleOffset, 0xff303030);
			// toggle
			Render2DUtils.drawSmoothRect(x+175, moduleOffset+1, x+185, moduleOffset+11, 0xff9f9f9f);
			Render2DUtils.drawSmoothRect(x+176, moduleOffset+2, x+184, moduleOffset+10, m.toggled?new Color(0, 255, 0).getRGB():-1);
			moduleOffset += 20;
		}
		Stencil.dispose();
		moduleSearch.xPos = x+295;
		moduleSearch.yPos = y+5;
		moduleSearch.drawScreen(mouseX, mouseY);
		// setting
		if(focusedModule != null) {
			Mania.instance.fontManager.medium12.drawString(focusedModule.name, x+205, y+40, 0xff303030);
			int settingOffset = y+55, i2 = 0;
			for(int i = 0; i < focusedModule.settings.size(); i++) {
				Mania.instance.fontManager.light10.drawString(focusedModule.settings.get(i).name, x+205, settingOffset, 0xff303030);
				if(focusedModule.settings.get(i) instanceof ModeSetting) {
					//modeBoxes.get(i2).drawScreen(x+290, settingOffset-5, 75, mouseX, mouseY);
					settingOffset += 17;
				}else if(focusedModule.settings.get(i) instanceof DoubleSetting) {
					DoubleSetting ds = (DoubleSetting) focusedModule.settings.get(i);
					float percentPos = (float) ((ds.value-ds.min/ds.max)*6);
					Render2DUtils.drawSmoothRect(x+295, settingOffset+3, x+395, settingOffset+4, 0xffcfcfcf);
					Render2DUtils.drawSmoothRect(x+295, settingOffset+3, x+295+percentPos, settingOffset+4, 0xffffcfcf);
					Render2DUtils.drawCircle(x+295+percentPos, settingOffset+3, 4, 0xffffcfcf);
					settingOffset += 17;
				}else if(focusedModule.settings.get(i) instanceof BooleanSetting) {
					Render2DUtils.drawSmoothRect(x+295, settingOffset, x+305, settingOffset+10, 0xff9f9f9f);
					Render2DUtils.drawSmoothRect(x+296, settingOffset+1, x+304, settingOffset+9, -1);
					if((boolean) focusedModule.settings.get(i).value) {
						
					}
					settingOffset += 17;
				}else if(focusedModule.settings.get(i) instanceof TextSetting) {
					
					settingOffset += 20;
				}else if(focusedModule.settings.get(i) instanceof ColorSetting) {
					settingOffset += 20;
				}
				i2++;
			}
		}
		// mode box
		if(focusedModule != null) {
			int settingOffset = y+55, i2 = 0;
			for(int i = 0; i < focusedModule.settings.size(); i++) {
				if(focusedModule.settings.get(i) instanceof ModeSetting) {
					modeBoxes.get(i2).drawScreen(x+290, settingOffset-5, 75, mouseX, mouseY);
					settingOffset += 17;
				}else if(focusedModule.settings.get(i) instanceof DoubleSetting) {
					settingOffset += 17;
				}else if(focusedModule.settings.get(i) instanceof BooleanSetting) {
					settingOffset += 17;
				}else if(focusedModule.settings.get(i) instanceof TextSetting) {
					settingOffset += 20;
				}else if(focusedModule.settings.get(i) instanceof ColorSetting) {
					settingOffset += 20;
				}
				i2++;
			}
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		int moduleOffset = y+40;
		// category
		int categoryOffset = y + 35;
		for(ModuleCategory c : ModuleCategory.values()) {
			if(ClickUtils.isMouseHovering(x, categoryOffset, animatedCategory, 20, mouseX, mouseY) && mouseButton == 0) {
				setCategory(c);
				return;
			}
			categoryOffset += 20;
		}
		// module
		for(Module m : modules) {
			if(ClickUtils.isMouseHovering(x+45+animatedExpand, moduleOffset, 185, 20, mouseX, mouseY)) {
				if(mouseButton == 1) {
					focusedModule = m;
					modeBoxes.clear();
					for(Setting s : focusedModule.settings) {
						if(s instanceof ModeSetting) {
							modeBoxes.add(new ModeSettingBox((ModeSetting) s));
						}
					}
					return;
				} else {
					m.toggle();
					return;
				}
			}
			moduleOffset += 20;
		}
		// settings
		if(focusedModule != null) {
			int settingOffset = y+55, i2 = 0;
			for(int i = 0; i < focusedModule.settings.size(); i++) {
				Mania.instance.fontManager.light10.drawString(focusedModule.settings.get(i).name, x+205, settingOffset, 0xff303030);
				if(focusedModule.settings.get(i) instanceof ModeSetting) {
					settingOffset += 17;
				}else if(focusedModule.settings.get(i) instanceof DoubleSetting) {
					DoubleSetting ds = (DoubleSetting) focusedModule.settings.get(i);
					float percentPos = (float) ((ds.value-ds.min/ds.max)*6);
					Render2DUtils.drawSmoothRect(x+295, settingOffset+3, x+395, settingOffset+4, 0xffcfcfcf);
					Render2DUtils.drawSmoothRect(x+295, settingOffset+3, x+295+percentPos, settingOffset+4, 0xffffcfcf);
					Render2DUtils.drawCircle(x+295+percentPos, settingOffset+3, 4, 0xffffcfcf);
					settingOffset += 17;
				}else if(focusedModule.settings.get(i) instanceof BooleanSetting) {
					BooleanSetting busu = (BooleanSetting) focusedModule.settings.get(i);
					if(mouseButton == 0 && ClickUtils.isMouseHovering(x+294, settingOffset-1, 6, settingOffset+11, mouseX, mouseY)) {
						busu.value = !busu.value;
						return;
					}
					settingOffset += 17;
				}else if(focusedModule.settings.get(i) instanceof TextSetting) {
					
					settingOffset += 20;
				}else if(focusedModule.settings.get(i) instanceof ColorSetting) {
					settingOffset += 20;
				}
				i2++;
			}
		}
		// mode setting
		for(ModeSettingBox msb : modeBoxes) msb.onClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		// TODO Auto-generated method stub
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected boolean isOn(int mouseX, int mouseY) {
		return true;
	}
	
	private void setCategory(ModuleCategory category) {
		modules = Mania.instance.moduleManager.getModulesBycCategory(category);
		//moduleScroll = new ScrollBall(0, 250, 50);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		// TODO Auto-generated method stub
		super.keyTyped(typedChar, keyCode);
	}

}
