package com.mania.gui.clickgui.mania;

import java.awt.Color;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.mania.Mania;
import com.mania.gui.clickgui.AbstractPanel;
import com.mania.management.font.TTFFontRenderer;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.ColorSetting;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.module.setting.Setting;
import com.mania.module.setting.TextSetting;
import com.mania.util.render.AnimationUtil;
import com.mania.util.render.ClickUtil;
import com.mania.util.render.ColorUtil;
import com.mania.util.render.IconUtil;
import com.mania.util.render.Render2DUtil;
import com.mania.util.render.ShadowUtil;
import com.mania.util.render.Stencil;

public class ManiaPanel extends AbstractPanel {
	
	private final TTFFontRenderer categoryFont, moduleFont, settingFont, iconFont;
	// scroll
	private int scroll, scrollAlpha;
	private float animateScroll;
	// setting
	private final boolean[] settingExpand;
	private final float[] animateSettingExpand;
	private Setting<?> focusedSetting; 
	
	public ManiaPanel(ModuleCategory category, int x, int y) {
		super(category, x, y);
		categoryFont = Mania.getFontManager().getFont("bold", 24);
		moduleFont = Mania.getFontManager().getFont("light", 22);
		settingFont = Mania.getFontManager().getFont("light", 20);
		iconFont = Mania.getFontManager().getFont("icon", 28);
		this.settingExpand = new boolean[super.modules.size()];
		this.animateSettingExpand = new float[super.modules.size()];
		
	}
	
	@Override
	public void drawPanel(int mouseX, int mouseY) {
		// tab
		Render2DUtil.rect(x, y, 100, 20, 0xffe0e0e0);
		{
			final int categoryWidth = (int) (categoryFont.getWidth(category.toString()) / 2);
			categoryFont.drawString(category.toString(), x + 50 - (categoryWidth) + 6, y + 2, 0xff5080ff);
			iconFont.drawString(category.ICON, x + 50 - categoryWidth - 12, y + 2, 0xff5080ff);
		}
		float offset = y + 20 + (animateScroll = AnimationUtil.animate(animateScroll, scroll));
		Stencil.pre();
		Render2DUtil.rect(x, y + 20, 100, 200);
		if (ClickUtil.isHovered(x, y + 20, 100, 250, mouseX, mouseY)) {
			final int wheel = Mouse.getDWheel();
			if (wheel == 0) {
				scrollAlpha = ColorUtil.updateAlpha(scrollAlpha, 5, false);
				float value = 200;
				for (int i = 0; i < animateSettingExpand.length; i++) value -= 20 + animateSettingExpand[i];
				if (value >= 0) value = 0;
				if (scroll <= value) scroll = (int) value;
				else if (scroll > 0) scroll = 0;
			} else {
				scrollAlpha = ColorUtil.updateAlpha(scrollAlpha, 25, true);
				if (wheel < 0) {
					float value = 200;
					for (int i = 0; i < animateSettingExpand.length; i++) value -= 20 + animateSettingExpand[i];
					if (value >= 0) value = 0;
					if (scroll > value) scroll += wheel / 10;
					else scroll = (int) value;
				} else if (wheel > 0) {
					if (scroll < 0) scroll += wheel / 10;
					else scroll = 0;
				}
			}
		} else scrollAlpha = ColorUtil.updateAlpha(scrollAlpha, 15, false);
		Stencil.post();
		// modules
		for (int i = 0, l = super.modules.size(); i < l; i++) {
			final Module m = modules.get(i);
			Render2DUtil.rect(x, offset, 100, 20, -1);
			moduleFont.drawString(m.getName(), x + 20, offset + 2.5f, m.isEnabled() ? 0xff5080ff : 0xff505060);
			if (m.isEnabled()) iconFont.drawString(IconUtil.CHECKED, x + 3, offset + 2, 0xff5080ff);
			// setting
			animateSettingExpand[i] = AnimationUtil.animate(animateSettingExpand[i], settingExpand[i] ? getHeight(m.getSettings()) : 0f);
			if (animateSettingExpand[i] != 0f) {
				if (!m.getSettings().isEmpty()) {
					final float startOffset = offset + 20f;
					for (Setting<?> s : m.getSettings()) {
						if (!s.isVisible()) continue;
						if (s instanceof ModeSetting) {
							final ModeSetting castSetting = (ModeSetting) s;
							Render2DUtil.rect(x, offset + 20, 100, 14, 0xfff0f0ff);
							s.animation = AnimationUtil.animate(s.animation, focusedSetting == s ? (castSetting.getOption().length + 0) * 14 : 0);
							if (s.animation != 0) {
								Render2DUtil.rect(x, offset + 20 + 14, 100, s.animation, 0xffe0e0f0);
								for (int mi = 0, ml = castSetting.getOption().length; mi < ml; mi++) {
									settingFont.drawString(castSetting.getOption()[mi], x + 10, offset + 20 + 14 + (mi * 14), 0xff505060);
									if (mi == castSetting.getIndex()) Render2DUtil.circle(x + 5, offset + 20 + 20.5f + (mi * 14), 1, 0xff505060);
								}
							}
							settingFont.drawString(String.format("%s :", s.getName()), x + 5, offset + 20, 0xff505060);
							settingFont.drawString(castSetting.getValue(), x  + 15 + settingFont.getWidth(s.getName()), offset + 20, 0xff5080ff);
							offset += s.animation + 14;
						} else if (s instanceof DoubleSetting) {
							final DoubleSetting castSetting = (DoubleSetting) s;
							Render2DUtil.rect(x, offset + 20, 100, 20, 0xfff0f0ff);
							settingFont.drawString(s.getName(), x + 5, offset + 20, 0xff505060);
							Render2DUtil.rect(x + 5, offset + 34, 90, 5, 0xff505060);
							Render2DUtil.rect(x + 5, offset + 34, (float) (90 * castSetting.getPercent()), 5, 0xff5080ff);
							final String formated = String.format("%s%s", castSetting.getValue(), castSetting.getUnit());
							settingFont.drawString(formated, x + 95 - settingFont.getWidth(formated), offset + 20, 0xff505060);
							offset += 20;
						} else if (s instanceof BooleanSetting) {
							Render2DUtil.rect(x, offset + 20, 100, 14, 0xfff0f0ff);
							settingFont.drawString(s.getName(), x + 5, offset + 20, 0xff505060);
							final boolean value = ((BooleanSetting) s).getValue();
							Render2DUtil.rect(x + 78.5f, offset + 21.5f, 20.5f, 10, 0xff505060);
							Render2DUtil.rect(x + 80 + (s.animation = AnimationUtil.animate(s.animation, value ? 0 : 10)), offset + 22.5f, 7.5f, 7.5f, value ? 0xff5080ff : -1);
							offset += 14;
						} else if (s instanceof TextSetting) {
							Render2DUtil.rect(x, offset + 20, 100, 28, 0xfff0f0ff);
							settingFont.drawString(s.getName(), x + 5, offset + 20, 0xff505060);
							Render2DUtil.rect(x + 5, offset + 44, 90, 0.5f, 0xff505060);
							offset += 28;
						} else if (s instanceof ColorSetting) {
							Render2DUtil.rect(x, offset + 20, 100, 64, 0xfff0f0ff);
							settingFont.drawString(s.getName(), x + 5, offset + 20, 0xff505060);
							offset += 64;
						}
					}
					//Render2DUtil.grad(x, startOffset, 100, 10, ShadowUtil.shadowColor, 0);
					offset = startOffset;
					offset += animateSettingExpand[i] - 20;
				}
			}
			offset += 20;
		}
		if (scrollAlpha != 0) {
			float value = 0;
			for (int i = 0; i < animateSettingExpand.length; i++) value += 20 + animateSettingExpand[i];
			Render2DUtil.rect(x + 95,  Math.max(y + 20 - (scroll / value) * 255, 5), 2.5f, 200 * (200 / value), new Color(80, 80, 96, scrollAlpha).getRGB()); // scroll bar
		}
		Render2DUtil.grad(x, y + 20, 100, 10, ShadowUtil.shadowColor, 0); // shadow
		Stencil.dispose();
		super.drawPanel(mouseX, mouseY);
	}
	
	@Override
	public void onClicked(int mouseX, int mouseY, int mouseButton) {
		if (ClickUtil.isHovered(x, y + 20, 100, 250, mouseX, mouseY)) {
			float offset = y + 20 + animateScroll;
			for (int i = 0, l = super.modules.size(); i < l; i++) {
				final Module m = modules.get(i);
				if (ClickUtil.isHovered(x, offset, 100, 20, mouseX, mouseY)) {
					if (mouseButton == 0) m.toggle();
					else {
						settingExpand[i] = !settingExpand[i];
						if (settingExpand[i]) m.getSettings().forEach(s -> s.animation = 0f);
					}
					return;
				}
				// setting
				if (settingExpand[i]) {
					if (!m.getSettings().isEmpty()) {
						for (Setting<?> s : m.getSettings()) {
							if (!s.isVisible()) continue;
							if (s instanceof ModeSetting) {
								if (ClickUtil.isHovered(x, offset + 20, 100, 14, mouseX, mouseY) && mouseButton == 1) {
									focusedSetting = focusedSetting == s ? null : s;
									return;
								}
								final ModeSetting castSetting = (ModeSetting) s;
								if (s.animation != 0) {
									for (int mi = 0, ml = castSetting.getOption().length; mi < ml; mi++) {
										if (mouseButton == 0 && ClickUtil.isHovered(x + 5, offset + 20 + 20.5f + (mi * 14), 100, 14, mouseX, mouseY)) {
											castSetting.setValue(mi);
											return;
										}
									}
								}
								offset += s.animation + 14;
							} else if (s instanceof DoubleSetting) {
								if (ClickUtil.isHovered(x + 5, offset + 34, 90, 5, mouseX, mouseY) && mouseButton == 0) {
									this.focusedSetting = s;
									return;
								}
								offset += 20;
							} else if (s instanceof BooleanSetting) {
								if (ClickUtil.isHovered(x, offset + 20, 100, 14, mouseX, mouseY)) {
									final BooleanSetting castSetting = (BooleanSetting) s;
									castSetting.setValue(!castSetting.getValue());
									return;
								}
								offset += 14;
							} else if (s instanceof TextSetting) {
								Render2DUtil.rect(x, offset + 20, 100, 28, 0xfff0f0ff);
								settingFont.drawString(s.getName(), x + 5, offset + 20, 0xff505060);
								Render2DUtil.rect(x + 5, offset + 44, 90, 0.5f, 0xff505060);
								offset += 28;
							} else if (s instanceof ColorSetting) {
								Render2DUtil.rect(x, offset + 20, 100, 64, 0xfff0f0ff);
								settingFont.drawString(s.getName(), x + 5, offset + 20, 0xff505060);
								offset += 64;
							}
						}
						//Render2DUtil.grad(x, startOffset, 100, 10, ShadowUtil.shadowColor, 0); shaodw
					}
				}
				offset += 20;
			}
		}
		super.onClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected boolean isOn(int mouseX, int mouseY) {
		return ClickUtil.isHovered(x, y, 100, 20, mouseX, mouseY);
	}
	
	private float getHeight(List<Setting<?>> settings) {
		if (!settings.isEmpty()) {
			int offset = 0;
			for (Setting<?> s : settings) {
				if (!s.isVisible()) continue;
				if (s instanceof ModeSetting) {
					offset += s.animation + 14;
				} else if (s instanceof DoubleSetting) {
					offset += 20;
				} else if (s instanceof BooleanSetting) {
					offset += 14;
				} else if (s instanceof TextSetting) {
					offset += 28;
				} else if (s instanceof ColorSetting) {
					offset += 64;
				}
			}
			return offset;
		}
		return 0;
	}

}
