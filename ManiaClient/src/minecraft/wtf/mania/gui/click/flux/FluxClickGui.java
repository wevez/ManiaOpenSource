package wtf.mania.gui.click.flux;

import java.io.IOException;
import java.util.List;
import java.util.Locale.Category;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import wtf.mania.Mania;
import wtf.mania.gui.click.GuiMoveable;
import wtf.mania.management.keybind.Keybind;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.data.Setting;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Stencil;

public class FluxClickGui extends GuiMoveable {
	
	private List<Module> modules;
	//category tab
	private ModuleCategory category;
	private int categoryIndex;
	private float animatedY;
	//scroll
	public int scrollY;
	private float animatedScrollY;
	//toggle button
	private float[] animatedButtons, animatedExpand;
	//setting expanded
	private boolean[] expands;
	private Module focusedModule;
	
	public FluxClickGui() {
		setCategory(ModuleCategory.Combat);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//base
		Render2DUtils.drawSmoothRectCustom(x, y, x+130, y+300, 20, 0xff1a1923);
		Mania.instance.fontManager.bold20.drawString(Mania.name, x+14, y+10, 0xfff8f9f9);
		Mania.instance.fontManager.medium12.drawString(Mania.version, x+73, y+17, 0xfff8f9f9);
		Render2DUtils.drawRect(x+110, y, x+120, y+300, 0xff1f1f25);
		Render2DUtils.drawSmoothRectCustom(x+110, y, x+425, y+300, 20, 0xff1f1f25);
		//category
		animatedY = dragging ? y+35+categoryIndex*25 : AnimationUtils.animate(animatedY, y+35+categoryIndex*25);
		Render2DUtils.drawSmoothRectCustom(x, animatedY, x+110, animatedY+25, 5, 0xff4586ff);
		for(int i = 0; i < ModuleCategory.values().length; i++) {
			Mania.instance.fontManager.light12.drawString(ModuleCategory.values()[i].toString(), x+30, y+42+i*25, i == categoryIndex ? -1 : 0xff323338);
			if(i != categoryIndex) ColorUtils.glColor(0xff323338);
			mc.getTextureManager().bindTexture(ModuleCategory.values()[i].icon);
			this.drawModalRectWithCustomSizedTexture(x+10, y+40+i*25, 0, 0, 16, 16, 16, 16);
		}
		//module
		Stencil.write(false);
		Render2DUtils.drawRect(x+110, y+5, x+425, y+300, 0xff1f1f25);
		Stencil.erase(true);
		if (Mouse.hasWheel()) {
			scrollY += Mouse.getDWheel()/10;
			if(scrollY > 0) scrollY = 0;
			if(-scrollY > modules.size()*20-20) scrollY = -modules.size()*20+20;
    	}
		animatedScrollY = AnimationUtils.animate(animatedScrollY, scrollY);
		float yOffset = animatedScrollY+y+10;
		for(int i = 0; i < modules.size(); i++) {
			if(modules.get(i) == focusedModule) {
				float predict = yOffset + getSize(modules.get(i).settings);
				Render2DUtils.drawSmoothRectCustom(x+120, yOffset, x+415, predict, 15, 0xff3c57c8);
				Render2DUtils.drawSmoothRectCustom(x+121, yOffset+1, x+414, predict-1, 15, 0xff2d2d33);
				Render2DUtils.drawOutlinedCricle(x+130, yOffset+18, 3, 0xff46474c, 0xffd1d2d7);
				Mania.instance.fontManager.light12.drawString(modules.get(i).name, x+137, yOffset+11, 0xff656577);
				//toggle
				Render2DUtils.drawSmoothRectCustom(x+370, yOffset+14, x+387, yOffset+23, 7, 0xffedeef4);
				Render2DUtils.drawOutlinedCricle(x+370, yOffset+18, 7, 0xff707176, modules.get(i).toggled ? 0xff3370ff : 0xfffbfbfb);
				//settings
				float xOffset = x+135;
				Mania.instance.fontManager.light10.drawString(focusedModule.disc, xOffset, yOffset+26, 0xff656577);
				yOffset += 40;
				for(Setting<?> s : modules.get(i).settings) {
					if(xOffset > x+300) {
						xOffset = x+135f;
						yOffset += 20;
					}
					if(s instanceof ModeSetting) {
						Mania.instance.fontManager.light10.drawString(s.name, xOffset, yOffset, 0xff3c57c8);
						ModeSetting md = (ModeSetting) s;
						yOffset += 15;
						for(String str : md.modes) {
							if(str.equals(md.value)) {
								Render2DUtils.drawOutlinedCricle(xOffset+4.5f, yOffset+4.5f, 4f, 0xff3c57c8, 0xff2d2d33);
								Render2DUtils.drawCircle(xOffset+4.5f, yOffset+4.5f, 3f, 0xff3c57c8);
							}else {
								Render2DUtils.drawOutlinedCricle(xOffset+4.5f, yOffset+4.5f, 4f, 0xff3c57c8, 0xff2d2d33);
							}
							Mania.instance.fontManager.light10.drawString(str, xOffset+10, yOffset, 0xff3c57c8);
							xOffset += Mania.instance.fontManager.light10.getWidth(str)+10;
						}
						xOffset = x+135;
						yOffset += 15;
					}else if(s instanceof DoubleSetting) {
						Mania.instance.fontManager.light10.drawString(s.name, xOffset, yOffset, 0xff3c57c8);
						DoubleSetting ds = (DoubleSetting) s;
						Render2DUtils.drawSmoothRect(xOffset, yOffset+13, xOffset+120, yOffset+14, 0xffceceda);
						float ballX = (float) ((ds.value-ds.min/ds.max)*6+xOffset);
						Render2DUtils.drawSmoothRect(xOffset, yOffset+13, ballX, yOffset+14, 0xff446adb);
						Render2DUtils.drawOutlinedCricle(ballX, yOffset+13, 3, 0xff38457c, 0xff446adb);
						xOffset += 135f;
					}else if(s instanceof BooleanSetting) {
						Mania.instance.fontManager.light10.drawString(s.name, xOffset+10, yOffset, 0xff3c57c8);
						Render2DUtils.drawSmoothRect(xOffset, yOffset+1, xOffset+8, yOffset+9, 0xffceceda);
						xOffset += 50;
					}
				}
				yOffset = predict+10;
			}else {
				Render2DUtils.drawSmoothRectCustom(x+120, yOffset, x+415, yOffset+35, 15, 0xff2d2d33);
				Render2DUtils.drawOutlinedCricle(x+130, yOffset+18, 3, 0xff46474c, 0xffd1d2d7);
				Mania.instance.fontManager.light12.drawString(modules.get(i).name, x+137, yOffset+11, 0xff656577);
				//toggle
				Render2DUtils.drawSmoothRectCustom(x+370, yOffset+14, x+387, yOffset+23, 7, 0xffedeef4);
				Render2DUtils.drawOutlinedCricle(x+370, yOffset+18, 7, 0xff707176, modules.get(i).toggled ? 0xff3370ff : 0xfffbfbfb);
				yOffset += 45;
			}
		}
		Stencil.dispose();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		//category
		for(int i = 0; i < ModuleCategory.values().length; i++) {
			if(ClickUtils.isMouseHovering(x, y+35+i*25, 110, 25, mouseX, mouseY)) {
				categoryIndex = i;
				setCategory(ModuleCategory.values()[i]);
				return;
			}
		}
		//modules
		float yOffset = animatedScrollY+y+10;
		for(int i = 0; i < modules.size(); i++) {
			if(modules.get(i) == focusedModule) {
				yOffset += getSize(modules.get(i).settings);
			}else {
				if(ClickUtils.isMouseHovering(x+120, yOffset, 415, 35, mouseX, mouseY))
					focusedModule = modules.get(i);
				yOffset += 45;
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		// TODO Auto-generated method stub
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	private void setCategory(ModuleCategory category) {
		modules = Mania.instance.moduleManager.getModulesBycCategory(category);
		animatedButtons = new float[modules.size()];
		expands = new boolean[modules.size()];
		animatedExpand = new float[modules.size()];
	}
	
	private float getSize(List<Setting> settings) {
		if(settings.size() == 0) return 45;
		else {
			float xOffset = x+135f, yOffset = 0;
			for(Setting s : settings) {
				if(xOffset > x+300) {
					xOffset = x+135f;
					yOffset += 20;
				}
				if(s instanceof ModeSetting) {
					ModeSetting md = (ModeSetting) s;
					yOffset += 15;
					for(String str : md.modes) {
						xOffset += Mania.instance.fontManager.light10.getWidth(str)+10;
					}
					xOffset = x+135;
					yOffset += 15;
				}else if(s instanceof DoubleSetting) {
					xOffset += 135f;
				}else if(s instanceof BooleanSetting) {

					xOffset += 50;
				}
			}
			return yOffset+55;
		}
	}

	@Override
	protected boolean isOn(int mouseX, int mouseY) {
		return true;
	}

}
