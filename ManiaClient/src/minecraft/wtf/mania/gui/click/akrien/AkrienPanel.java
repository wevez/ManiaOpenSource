package wtf.mania.gui.click.akrien;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import wtf.mania.MCHook;
import wtf.mania.Mania;
import wtf.mania.gui.click.AbstractPanel;
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
import wtf.mania.util.render.ShaderStencil;

public class AkrienPanel extends AbstractPanel implements MCHook {
	
	private static final int WIDTH = 100;
	
	private static final ResourceLocation color;

	private final List<Module> modules;
	private final ModuleCategory category;
	
	private final boolean[] settingExpand;
	private final float[] animatedSetting;
	private final int[] toggledAlpha;
	
	private boolean expand;
	private float animatedExpand;
	
	private DoubleSetting draggingDouble;
	
	public static int background, tab, white, liteBlue, darkBlue, grey, darkWhite;
	
	static {
		color = new ResourceLocation("mania/color.png");
	}
	
	public AkrienPanel(int x, int y, ModuleCategory category) {
		super(x, y);
		this.modules = Mania.instance.moduleManager.getModulesBycCategory(category);
		this.category = category;
		this.settingExpand = new boolean[modules.size()];
		this.animatedSetting = new float[modules.size()];
		this.toggledAlpha = new int[modules.size()];
		this.expand = true;
	}
	
	@Override
	public void drawPanel(int mouseX, int mouseY) {
		// tab
		Render2DUtils.drawRect(x - 1, y - 1, x + WIDTH + 1, y + 20, tab);
		mc.getTextureManager().bindTexture(this.category.icon);
		GlStateManager.enableAlpha();
		ColorUtils.glColor(white);
		Gui.drawModalRectWithCustomSizedTexture(x + 5, y + 2, 0, 0, 16, 16, 16, 16);
		Mania.instance.fontManager.medium12.drawString(category.name(), x + 30, y + 4, white);
		// module list
		{
			float wa = 0f;
			for (float f : this.animatedSetting) {
				wa += f;
			}
			this.animatedExpand = AnimationUtils.animate(this.animatedExpand, this.expand ? wa + 12.5f * modules.size() + 2.5f : 0f);
		}
		float offset = 0f;
		//Render2DUtils.drawRect(x - 1, y + 25 - 1, x + 5, y + 25 + 15.5f * modules.size(), background);
		//Render2DUtils.drawRect(x + WIDTH - 5, y + 25 - 1, x + WIDTH + 1, y + 25 + 15.5f * modules.size(), background);
		for (int i = 0; i < modules.size(); i++) {
			final Module m = modules.get(i);
			/*if (m.toggled) {
				if (this.toggledAlpha[i] != 255) this.toggledAlpha[i] += 15;
				else this.toggledAlpha[i] -= 15;
			}*/
			Mania.instance.fontManager.medium9.drawCenteredString(m.name, x + WIDTH / 2, y + 20 + offset, m.toggled ? liteBlue : white);
			if (this.settingExpand[i]) {
				this.animatedSetting[i] = AnimationUtils.animate(this.animatedSetting[i], getSettingheight(m.settings));
			} else {
				this.animatedSetting[i] = AnimationUtils.animate(this.animatedSetting[i], 0f);
			}
			// setting
			if (this.animatedSetting[i] != 0 && !m.settings.isEmpty()) {
				float settingOffset = 0f;
				for (Setting<?> s : m.settings) {
					if (settingOffset >= this.animatedSetting[i]) break;
					if (s instanceof ModeSetting) {
						final ModeSetting mobu = (ModeSetting) s;
						final float modeHeight = mobu.expanded ? mobu.modes.size() * 12.5f : 15;
						Render2DUtils.drawRect(x + 5, settingOffset + y + 12.5f + 20 + offset, x + WIDTH - 5, settingOffset + y + offset + 20 + 12.5f + modeHeight, 0x10ffffff);
						Render2DUtils.drawRect(x + 5, settingOffset + y + 12.5f + 20 + offset, x + 7.5f, settingOffset + y + 12.5f + 20 + offset + modeHeight, grey);
						Render2DUtils.drawRect(x + WIDTH - 7.5f, settingOffset + y + 12.5f + 20 + offset, x + WIDTH - 5, settingOffset + y + 12.5f + 20 + offset + modeHeight, grey);
						Mania.instance.fontManager.medium9.drawString(mobu.name, x + 10, settingOffset + y + 15 + offset + 20, darkWhite);
						final String value = mobu.value;
						if (mobu.expanded) {
							for (int mode = 0, l = mobu.modes.size(); mode < l; mode++) {
								Mania.instance.fontManager.medium9.drawCenteredString(mobu.modes.get(mode), x + WIDTH / 2, settingOffset + y + 15 + offset + 20, mode == mobu.index ? liteBlue : grey);
								settingOffset += 12.5f;
							}
							settingOffset += 5;
						} else {
							Mania.instance.fontManager.medium9.drawString(value, x + WIDTH - Mania.instance.fontManager.medium9.getWidth(value) - 10, settingOffset + y + 15 + offset + 20, liteBlue);
							settingOffset += 20;
						}
					} else if (s instanceof DoubleSetting) {
						final DoubleSetting debu = (DoubleSetting) s;
						Mania.instance.fontManager.medium9.drawString(debu.name, x + 5, settingOffset + y + 10 + offset + 20, darkWhite);
						final String formated = String.format("%d %s", debu.value.intValue(), debu.unit);
						Mania.instance.fontManager.medium9.drawString(formated, x - 5 + WIDTH - Mania.instance.fontManager.medium9.getWidth(formated), settingOffset + y + 10 + offset + 20, darkWhite);
						Render2DUtils.drawRect(x + 5,  settingOffset + y + 10 + offset + 20 + 10, x + WIDTH - 5,  settingOffset + y + 10 + offset + 20 + 12.5f, grey);
						Render2DUtils.drawRect(x + 5,  settingOffset + y + 10 + offset + 20 + 10, x + 5 + (float) MiscUtils.getPercent(debu) * (WIDTH - 5),  settingOffset + y + 10 + offset + 20 + 12.5f, liteBlue);
						if (this.draggingDouble == debu) {
							MiscUtils.setDoubleValue(draggingDouble, x + 5,  settingOffset + y + 10 + offset + 20 + 10, WIDTH - 5, mouseX, mouseY);
						}
						settingOffset += 17.5f;
					} else if (s instanceof BooleanSetting) {
						final BooleanSetting busu = (BooleanSetting) s;
						Mania.instance.fontManager.medium9.drawString(busu.name, x + 5, settingOffset + y + 10 + offset + 20, darkWhite);
						busu.case0 = AnimationUtils.animate(busu.case0, busu.value ? 20 : 10);
						Render2DUtils.drawRect(x + 80, settingOffset + y + 11.5f + offset + 20, x + 95, settingOffset + y + 9.5f + offset + 28, busu.value ? darkBlue : darkWhite);
						Render2DUtils.drawRect(x + 80 + busu.case0 - 10, settingOffset + y + 9.5f + offset + 20, x + 80 + busu.case0 - 5, settingOffset + y + 9.5f + offset + 30, busu.value ? liteBlue : darkWhite);
						settingOffset += 15;
					} else if(s instanceof TextSetting) {
						final TextSetting unti = (TextSetting) s;
						Mania.instance.fontManager.medium9.drawString(unti.name, x + 5, settingOffset + y + 10 + offset + 20, darkWhite);
						
						settingOffset += 20;
					} else if (s instanceof ColorSetting) {
						final ColorSetting unko = (ColorSetting) s;
						Mania.instance.fontManager.medium9.drawString(unko.name, x + 5, settingOffset + y + 10 + offset + 20, darkWhite);
						Render2DUtils.drawRect(x + 5, settingOffset + y + 10 + offset + 30, x + 50, settingOffset + y + 10 + offset + 40, unko.value.getRGB());
						settingOffset += 50;
					}
				}
			}
			offset += 12.5f + this.animatedSetting[i];
		}
		Render2DUtils.drawGradient(x - 1, y + 20 + animatedExpand, x + WIDTH + 1, y +  20 + animatedExpand + 1, darkBlue, liteBlue);
		super.drawPanel(mouseX, mouseY);
	}
	
	public void preBlur(int mouseX, int mouseY) {
		Render2DUtils.drawRect(x, y, x + WIDTH, y + 20, tab);
		Render2DUtils.drawGradient(x - 2.5f, y + 20 + animatedExpand, x + WIDTH + 1, y + 20 + animatedExpand + 2.5f, darkBlue, liteBlue);
		// modules
		float offset = 0f;
		for (int i = 0; i < modules.size(); i++) {
			final Module m = modules.get(i);
			if (m.toggled) {
				Mania.instance.fontManager.medium13.drawCenteredString(m.name, x + WIDTH / 2, y + 20 + offset, liteBlue);
			}
			if (this.animatedSetting[i] != 0 && !m.settings.isEmpty()) {
				float settingOffset = 0f;
				Render2DUtils.drawRect(x, y + offset + 15 + 17.5f, x + WIDTH, y + offset + getSettingheight(m.settings) + 15 + 17.5f, tab);
				for (Setting s : m.settings) {
					if (settingOffset > this.animatedSetting[i]) break;
					if (s instanceof BooleanSetting) {
						if (((BooleanSetting) s).value) {
							Render2DUtils.drawRect(x + 80, settingOffset + y + 11.5f + offset + 20, x + 95, settingOffset + y + 9.5f + offset + 28, darkBlue);
							Render2DUtils.drawRect(x + 80 + s.case0 - 10, settingOffset + y + 9.5f + offset + 20, x + 80 + s.case0 - 5, settingOffset + y + 9.5f + offset + 30, liteBlue);
						}
						settingOffset += 15;
					} else if (s instanceof DoubleSetting) {
						final DoubleSetting debu = (DoubleSetting) s;
						Render2DUtils.drawRect(x + 5,  settingOffset + y + 10 + offset + 20 + 10, x + 5 + (float) (debu.value / debu.max) * (WIDTH - 5),  settingOffset + y + 10 + offset + 20 + 12.5f, liteBlue);
						settingOffset += 17.5f;
					} else if (s instanceof ModeSetting) {
						final ModeSetting mobu = (ModeSetting) s;
						final String value = mobu.value;
						if (mobu.expanded) {
							for (int mode = 0, l = mobu.modes.size(); mode < l; mode++) {
								if (mode == mobu.index) Mania.instance.fontManager.medium9.drawCenteredString(mobu.modes.get(mode), x + WIDTH / 2, settingOffset + y + 15 + offset + 20, liteBlue);
								settingOffset += 12.5f;
							}
							settingOffset += 5;
						} else {
							Mania.instance.fontManager.medium9.drawString(value, x + WIDTH - Mania.instance.fontManager.medium9.getWidth(value) - 10, settingOffset + y + 15 + offset + 20, liteBlue);
							settingOffset += 20;
						}
					} else if (s instanceof ColorSetting) {
						final ColorSetting unko = (ColorSetting) s;
						
						Render2DUtils.drawRect(x + 5, settingOffset + y + 10 + offset + 30, x + 50, settingOffset + y + 10 + offset + 40, unko.value.getRGB());
						settingOffset += 50;
					} else if (s instanceof TextSetting) {
						
						settingOffset += 20;
					}
				}
			}
			offset += 12.5f + this.animatedSetting[i];
		}
	}
	
	public void stencil() {
		final float animation = AkrienClickGui.animatedInit * 0.1f;
		if (animation == 1f) Render2DUtils.drawRect(x - 1, y - 1, x + WIDTH + 1, y + 20 + this.animatedExpand, -1);
		else {
			final float sWidth = WIDTH / 2f, sHeight = this.animatedExpand / 2f;
			Render2DUtils.drawRect(x + sWidth - animation * sWidth, this.y + sHeight - sHeight * animation, x + sWidth + animation * sWidth, y + sHeight + sHeight * animation, -1);
		}
	}
	
	@Override
	public void onClicked(int mouseX, int mouseY, int mouseButton) {
		float offset = 0f;
		// modules
		for (int i = 0; i < modules.size(); i++) {
			final Module m = modules.get(i);
			if (ClickUtils.isMouseHovering(x - 1, y + 20 + offset, WIDTH, 15, mouseX, mouseY)) {
				if (mouseButton == 0) {
					m.toggle();
				} else {
					this.settingExpand[i] = !this.settingExpand[i];
				}
				return;
			}
			if (this.animatedSetting[i] != 0 && !m.settings.isEmpty()) {
				Render2DUtils.drawRect(x, y + offset + 15 + 17.5f, x + WIDTH, y + offset + getSettingheight(m.settings) + 15 + 17.5f, tab);
				for (Setting s : m.settings) {
					if (s instanceof BooleanSetting) {
						final BooleanSetting unko = (BooleanSetting) s;
						if (ClickUtils.isMouseHovering2(x + 80, y + 11.5f + offset + 20, x + 95, y + 9.5f + offset + 28, mouseX, mouseY)) {
							unko.value = !unko.value;
						}
						offset += 15;
					} else if (s instanceof DoubleSetting) {
						final DoubleSetting debu = (DoubleSetting) s;
						if (ClickUtils.isMouseHovering2(x + 5,  y + 10 + offset + 20 + 10, x + 5 + (WIDTH - 5),  y + 10 + offset + 20 + 12.5f, mouseX, mouseY)) {
							this.draggingDouble = debu;
							
							return;
						}
						offset += 17.5f;
					} else if (s instanceof ModeSetting) {
						final ModeSetting unti = (ModeSetting) s;
						if (unti.expanded) {
							if (mouseButton == 0) {
								float modeOffset = 0f;
								for (int mode = 0; mode < unti.modes.size(); mode++) {
									if (ClickUtils.isMouseHovering(x + 5,  y + 12.5f + 20 + offset + modeOffset, WIDTH - 5, 12.5f, mouseX, mouseY)) {
										unti.setValue(unti.modes.get(mode));
										unti.parentModule.onSetting();
										return;
									}
									modeOffset += 12.5f;
								}
							} else {
								if (ClickUtils.isMouseHovering(x + 5, y + 12.5f + 20 + offset, WIDTH - 5, unti.modes.size() * 12.5f + 5, mouseX, mouseY)) {
									unti.expanded = !unti.expanded;
									return;
								}
							}
						} else {
							if (ClickUtils.isMouseHovering2(x + 5, y + 12.5f + 20+ offset, x + WIDTH - 5, y + offset + 27.5f + 20, mouseX, mouseY)) {
								if (mouseButton == 1) {
									unti.expanded = !unti.expanded;
									return;
								}
							}
						}
						offset += unti.expanded ? unti.modes.size() * 12.5f : 20;
					} else if (s instanceof ColorSetting) {
						final ColorSetting unko = (ColorSetting) s;
						Render2DUtils.drawRect(x + 5, y + 10 + offset + 30, x + 50, y + 10 + offset + 40, unko.value.getRGB());
						offset += 50;
					} else if (s instanceof TextSetting) {
						offset += 20;
					}
				}
			}
			offset += 12.5f;
		}
		super.onClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		this.draggingDouble = null;
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	private float getSettingheight(List<Setting> settings) {
		if (!settings.isEmpty()) {
			float offset = 0f;
			for (Setting s : settings) {
				if (s instanceof BooleanSetting) {
					offset += 15;
				} else if (s instanceof DoubleSetting) {
					final DoubleSetting debu = (DoubleSetting) s;
					offset += 17.5f;
				} else if (s instanceof ModeSetting) {
					final ModeSetting mobu = (ModeSetting) s;
					final String value = mobu.value;
					if (mobu.expanded) {
						for (int mode = 0, l = mobu.modes.size(); mode < l; mode++) {
							offset += 12.5f;
						}
						offset += 5;
					} else {
						offset += 20;
					}
				} else if (s instanceof ColorSetting) {
					final ColorSetting unko = (ColorSetting) s;
					offset += 50;
				} else if (s instanceof TextSetting) {
					
					offset += 20;
				}
			}
			return offset;
		} else {
			return 0;
		}
	}

	@Override
	protected boolean isOn(int mouseX, int mouseY) {
		return ClickUtils.isMouseHovering(x, y, WIDTH, 100, mouseX, mouseY);
	}

}
