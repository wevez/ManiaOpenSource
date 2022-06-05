package wtf.mania.gui.click.ryo;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.player.EntityPlayer;
import wtf.mania.Mania;
import wtf.mania.gui.box.TextBox2;
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
import wtf.mania.util.MiscUtils;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Stencil;

public class RyoClickGui extends GuiMoveable {
	
	// window size
	private static final int WIDTH = 500, HEIGHT = 300;
	
	// category
	private ModuleCategory category;
	// module
	private List<Module> modules;
	private boolean[] moduleExpand;
	private float[] animatedModuleExpand;
	private float[] animatedToggle;
	// setting
	private Module module;
	private Setting<?> setting;
	// search bar
	private TextBox2 search;
	// scroll
	private int scroll;
	private float animatedScroll;
	
	public RyoClickGui() {
		category = ModuleCategory.Combat;
		this.setCategory();
		search = new TextBox2("Search module", 0, 0, 100, 20, "", Mania.instance.fontManager.light10);
	}
	
	private void setCategory() {
		modules = Mania.instance.moduleManager.getModulesBycCategory(category);
		moduleExpand = new boolean[modules.size()];
		animatedModuleExpand = new float[modules.size()];
		animatedToggle = new float[modules.size()];
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// get scroll
		final float currentScroll = Mouse.getDWheel() * 0.1f;
		// handle scroll
		if (currentScroll != 0) {
			if (currentScroll > 0) {
				// up
				if (scroll < 0)
				scroll += currentScroll;
			} else {
				// down
				float maxHeight = modules.size() * 40;
				if (module != null) {
					maxHeight += getSettingsSize(module) - 40;
				}
				if (scroll-250 > -maxHeight)
					scroll += currentScroll;
			}
		}
		animatedScroll = AnimationUtils.animate(animatedScroll, scroll);
		// render base background
		Render2DUtils.drawSmoothRect(x, y, x + WIDTH, y + HEIGHT, 0xffeff4f9);
		Render2DUtils.drawGradientSideways(x + 125, y, x + 150, y + HEIGHT, 0xffa0a1a2, 0xffeff4f9);
		// render current view as string
		Mania.instance.fontManager.light15.drawString(category.name(), x + 150, y + 20, 0xff5b5d5f);
		if (module != null) {
			final float categoryWidth = Mania.instance.fontManager.light15.getWidth(category.name());
			Mania.instance.fontManager.light15.drawString(">", x + 155 + categoryWidth, y + 19, 0xff4d4e4e);
			Mania.instance.fontManager.light15.drawString(module.name, x + 170 + categoryWidth, y + 20, 0xff5d5d5e);
			// render description
			Mania.instance.fontManager.light10.drawString(module.disc, x + WIDTH - Mania.instance.fontManager.light10.getWidth(module.disc) - 5, y + 25.5f, 0xff5d5d5e);
		}
		// render player icon
		//Render2DUtils.drawCircle(x + 30, y + 30, 15, -1); (extreme circle)
		Stencil.write(false);
		Render2DUtils.drawSmoothRectCustom(x + 15, y + 15, x + 45, y + 45, 25, 0xc0ffffff);
		Stencil.erase(true);
		final String UUID = mc.player.getGameProfile().getId().toString();
    	ThreadDownloadImageData ab = AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(UUID), UUID);
    	try {
			ab.loadTexture(mc.getResourceManager());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	mc.getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(UUID));
		Gui.drawScaledCustomSizeModalRect(x + 15, y + 15, 8.0F, 8, 8, 8, 30, 30, 64.0F, 64.0F);
		Render2DUtils.drawRect(x + 15, y + 15, x + 45, y + 45, 0x25ffffff);
		Stencil.dispose();
		// render id / user name
		Mania.instance.fontManager.light10.drawString(mc.player.getName(), x + 52.5f, y + 19, 0xff000000);
		Mania.instance.fontManager.light10.drawString(Mania.user, x + 52.5f, y + 30, 0xff000000);
		// search bar background
		Render2DUtils.drawSmoothRect(x + 15, y + 60 - 2.5f, x + 15 + 100, y + 60 + 17.5f - 2.5f, 0xffe1e6ea);
		Render2DUtils.drawSmoothRect(x + 16, y + 61 - 2.5f, x + 14 + 100, y + 59 + 17.5f - 2.5f, -1);
		// render module search bar
		search.xPos = x + 14;
		search.yPos = y + 54;
		search.drawScreen(mouseX, mouseY);
		// render scroll bar
		Render2DUtils.drawLine(x + WIDTH - 12.5f, y + 50, x + WIDTH - 12.5f, y + 150, 2.5f, 0xff85868a);
		// render category bar
		float offset = 0f;
		for (int i = 0; i < ModuleCategory.values().length; i++) {
			// category icon
			ColorUtils.glColor(0xff1f9fcf);
			Render2DUtils.drawImage(ModuleCategory.values()[i].icon, x + 20, y + 85 + (int) offset, 16, 16);
			Mania.instance.fontManager.light11.drawString(ModuleCategory.values()[i].name(), x + 47.5f, y + 87.5f + (int) offset, 0xff5b5d5f);
			offset += 26;
		}
		offset = animatedScroll;
		// render module list
		int index = 0;
		Stencil.write(false);
		Render2DUtils.drawSmoothRect(x, y + 50, x + WIDTH, y + HEIGHT, 0xffeff4f9);
		Stencil.erase(true);
		for (Module m : modules) {
			// calculate setting frame
			if (moduleExpand[index]) {
				animatedModuleExpand[index] = AnimationUtils.animate(animatedModuleExpand[index], getSettingsSize(m));
			} else {
				animatedModuleExpand[index] = AnimationUtils.animate(animatedModuleExpand[index], 40);
			}
			// render module frame
			Render2DUtils.drawSmoothRect(x + 149, y + offset + 49, x + WIDTH - 24, y + 51 + animatedModuleExpand[index] + offset, 0xffe1e6ea);
			Render2DUtils.drawSmoothRect(x + 150, y + offset + 50, x + WIDTH - 25, y + 50 + animatedModuleExpand[index] + offset, 0xfffbfbfd);
			// render something like icon
			Render2DUtils.drawSmoothRectCustom(x + 167.5f, y + offset + 62.5f, x + 182.5f, y + offset + 77.5f, 7.5f, 0xff0078d4);
			Mania.instance.fontManager.light10.drawString(m.name, x + 200, y + offset + 65, m.toggled ? 0xff1a1b1b : 0xffa0a0a2);
			// render setting expand bar
			if (!m.settings.isEmpty()) {
				Render2DUtils.drawLine(x + WIDTH - 47.5f, y + offset + 67.5f, x + WIDTH - 42.5f, y + offset + 72.5f, 3, 0xff494949);
				Render2DUtils.drawLine(x + WIDTH - 52.5f, y + offset + 72.5f, x + WIDTH - 47.5f, y + offset + 67.5f, 3, 0xff494949);
			}
			// render toggle bar
			Render2DUtils.drawSmoothRectCustom(x + WIDTH - 90 - 7.5f, y + offset + 63f, x + WIDTH - 60 - 5, y + offset + 77f, 15, m.toggled ? 0xff5d5d5e : 0xffc9c9ca);
			Render2DUtils.drawSmoothRectCustom(x + WIDTH - 89.5f - 7.5f, y + offset + 63.5f, x + WIDTH - 60.5f - 5, y + offset + 76.5f, 15, 0xfff5f5f7);
			animatedToggle[index] = AnimationUtils.animate(animatedToggle[index], m.toggled ? -10 : 7.5f);
			Render2DUtils.drawCircle(x + WIDTH - 80 + animatedToggle[index], y + offset + 70, 5, 0xff5d5d5e);
			// render things of setting
			if (animatedModuleExpand[index] != 40) {
				float settingOffset = 0f;
				for (Setting<?> s : m.settings) {
					if (!s.visibility.get()) continue;
					// render setting name
					Mania.instance.fontManager.light10.drawString(s.name, x + 167, y + offset + 80 + settingOffset, 0xffa0a0a2);
					final float stringWidth = Mania.instance.fontManager.light10.getWidth(s.name);
					if (s instanceof BooleanSetting) {
						final BooleanSetting debu = (BooleanSetting) s;
						debu.case0 = AnimationUtils.animate(debu.case0, debu.value ? 0 : 10f);
						// render check box
						Render2DUtils.drawSmoothRectCustom(x + 167, y + offset + settingOffset + 80 + 12, x + 200, y + offset + settingOffset + 80 + 25, 12.5f, debu.value ? 0xff5d5d5e : 0xffa0a0a2);
						Render2DUtils.drawSmoothRectCustom(x + 168, y + offset + settingOffset + 80 + 13, x + 199, y + offset + settingOffset + 80 + 24, 12.5f, -1);
						Render2DUtils.drawCircle(x + 174 + debu.case0, y + offset + settingOffset + 80 + 18.5f, 5, debu.value ? 0xff5d5d5e : 0xffa0a0a2);
						settingOffset += 26;
					} else if (s instanceof DoubleSetting) {
						final DoubleSetting debu = (DoubleSetting) s;
						// render value bar
						Render2DUtils.drawLine(x + 167 , y + offset + 80 + 15 + settingOffset, x + 167 + 100, y + offset + 15 + 80 + settingOffset, 1, 0xffa0a0a2);
						final float valuePos = (float) MiscUtils.getPercent(debu) * 100;
						Render2DUtils.drawSmoothRectCustom(x + 167 + valuePos, y + offset + 80 + 15 + settingOffset - 5, x + 167 + valuePos + 2, y + offset + 80 + 15 + settingOffset + 5, 2, 0xffa0a0a2);
						settingOffset += 28f;
					} else if (s instanceof ModeSetting) {
						// render mode box
						Render2DUtils.drawRect(x + 167 + stringWidth + 2.5f, y + offset + 80 + settingOffset - 1, x + 167 + stringWidth + 2.5f + 100, y + offset + 80 + settingOffset + 12, 0xffa0a0a2);
						Render2DUtils.drawRect(x + 168 + stringWidth + 2.5f, y + offset + 80 + settingOffset, x + 167 + stringWidth + 2.5f + 100 - 1, y + offset + 80 + settingOffset + 12 - 1, -1);
						Mania.instance.fontManager.light7.drawString(((ModeSetting) s).value, x + 167 + stringWidth + 5, y +offset + 80 + settingOffset + 2, 0xffa0a0a2);
						settingOffset += 14f;
					} else if (s instanceof ColorSetting) {
						
					} else if (s instanceof TextSetting) {
						
					}
				}
			}
			offset += animatedModuleExpand[index] + 1;
			index++;
		}
		Stencil.dispose();
		offset = 0f;
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		float offset = 0f;
		// category checks
		
		// module checks
		int index = 0;
		offset = animatedScroll;
		for (Module m : modules) {
			if (ClickUtils.isMouseHovering(x + 149, y + offset + 49, WIDTH - 24 - 149, 91 - 49 + offset, mouseX, mouseY)) {
				if (mouseButton == 0) {
					m.toggle();
					
				} else {
					moduleExpand[index] = !moduleExpand[index];
					// clear other setting window
					for (int i = 0, l = moduleExpand.length; i < l; i++) {
						if (i != index) {
							moduleExpand[i] = false;
						}
					}
					if (moduleExpand[index]) module = m;
					else module = null;
				}
				return;
			}
			if (moduleExpand[index])
				offset += getSettingsSize(m);
			else
				offset += 40;
			index++;
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected boolean isOn(int mouseX, int mouseY) {
		return ClickUtils.isMouseHovering(x, y, WIDTH, HEIGHT, mouseX, mouseY);
	}
	
	private float getSettingsSize(Module module) {
		float current = 40;
		for (Setting<?> s : module.settings) {
			if (s.visibility.get()) {
				current += 24f;
			}
		}
		return current;
	}

}
