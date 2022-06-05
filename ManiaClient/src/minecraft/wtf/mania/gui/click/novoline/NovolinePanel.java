package wtf.mania.gui.click.novoline;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
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
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Stencil;

public class NovolinePanel extends AbstractPanel {
	
	private final ModuleCategory category;
	
	private final List<Module> modules;
	
	private float animatedExpand;
	private boolean expanded;
	public float animatedDoubleLimit;
	
	private final float[] animatedSetting;
	private final boolean[] settingExpanded;
	
	private final int[] colors;
	public int limitedColor;
	
	private Setting focusedSetting;
	
	public NovolinePanel(int x, int y, ModuleCategory category) {
		super(x, y);
		expanded = true;
		this.category = category;
		modules = Mania.instance.moduleManager.getModulesBycCategory(category);
		animatedSetting = new float[modules.size()];
		settingExpanded = new boolean[modules.size()];
		colors = new int[modules.size()];
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		focusedSetting = null;
		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	public void drawPanel(int mouseX, int mouseY) {
		animatedDoubleLimit = AnimationUtils.animate(animatedDoubleLimit, 90, 0.925f);
		animatedExpand = AnimationUtils.animate(animatedExpand, getSize());
		// base
		//Render2DUtils.drawRect(x, y, x+95, y+animatedExpand, 0xff202020);
		// category
		Render2DUtils.drawRect(x+1, y+1, x+94, y+15, 0xff252525);
		Mania.instance.fontManager.medium12.drawStringWithShadow(category.toString(), x+3f, y+1.25f, 0xffe0e0e0);
		Render2DUtils.drawImage(category.icon, x+80, y+2, 12, 12);
		// module
		Render2DUtils.drawRect(x+1, y+15, x+94, y+animatedExpand-1, 0xff303030);
		Stencil.write(false);
		Render2DUtils.drawRect(x+1, y, x+94, y+animatedExpand-1, 0xff303030);
		Stencil.erase(true);
		{
			int offset = y+15;
			for (int i = 0; i < modules.size(); i++) {
				// toggle
				if(modules.get(i).toggled) {
					colors[i] = colors[i] == 256 ? 256 : colors[i]+16;
				}else {
					colors[i] = colors[i] == 0 ? 0 : colors[i]-16;
				}
				Render2DUtils.drawRect(x+1, offset, x+94, offset+13, 0xff303030);
				Render2DUtils.drawRect(x+1, offset, x+94, offset+13, limitedColor == 256 ? colors[i] == 0 ? 0xff303030 : colors[i] == 255 ? 0xffee6060 : new Color(255, 96, 96, colors[i]-1).getRGB() : modules.get(i).toggled && limitedColor != 0 ? new Color(255, 96, 96, limitedColor-1).getRGB() : 0xff303030);
				Mania.instance.fontManager.medium9.drawStringWithShadow(modules.get(i).name, x+2.5f, offset+1.5f, 0xffe0e0e0);
				if(!modules.get(i).settings.isEmpty()) {
					if(settingExpanded[i]) {
						Render2DUtils.drawLine(x+86, offset+9, x+89, offset+3, 2, 0xffe0e0e0);
						Render2DUtils.drawLine(x+89, offset+3, x+92, offset+9, 2, 0xffe0e0e0);
					}else {
						Render2DUtils.drawLine(x+86, offset+3, x+89, offset+9, 2, 0xffe0e0e0);
						Render2DUtils.drawLine(x+89, offset+9, x+92, offset+3, 2, 0xffe0e0e0);
					}
				}
				// setting
				int sOffset = 13;
				for(Setting s : modules.get(i).settings) {
					boolean focusedDope = focusedSetting == s;
					if(sOffset >= animatedSetting[i]) break;
					if(!(boolean) s.visibility.get()) continue;
					Mania.instance.fontManager.medium9.drawStringWithShadow(s.name, x+3, offset+sOffset, 0xffeeeeee);
					if(s instanceof BooleanSetting) {
						Render2DUtils.drawRect(x+77.5f, offset+sOffset, x+95, offset+sOffset+13, 0xff303030);
						Render2DUtils.drawSmoothRect(x+84, offset+sOffset, x+83+10, offset+sOffset+10, 0xff202020);
						if((boolean) s.value) {
							Render2DUtils.drawLine(x+85, offset+sOffset+5, x+83+5, offset+sOffset+7, 3, 0xffe0e0e0);
							Render2DUtils.drawLine(x+83+5, offset+sOffset+7, x+83+9, offset+sOffset+2, 3, 0xffe0e0e0);
						}
						sOffset += 13;
					}else if(s instanceof ModeSetting) {
						ModeSetting obusu = (ModeSetting) s;
						Render2DUtils.drawRect(x+90-Mania.instance.fontManager.medium9.getWidth(obusu.value.toUpperCase()), offset+sOffset, x+94, offset+sOffset+13, 0xff303030);
						Mania.instance.fontManager.medium9.drawStringWithShadow(obusu.value.toUpperCase(), x+94-Mania.instance.fontManager.medium9.getWidth(obusu.value.toUpperCase()), offset+sOffset, 0xffe0e0e0);
						sOffset += 13;
					}else if(s instanceof DoubleSetting) {
						DoubleSetting debu = (DoubleSetting) s;
						float width = (float) MiscUtils.getPercent(debu)*90;
						Render2DUtils.drawRect(x+2.5f, offset+sOffset+10.5f, x+90, offset+sOffset+21, 0xff202020);
						Render2DUtils.drawRect(x+2.5f, offset+sOffset+10.5f, width > animatedDoubleLimit ? animatedDoubleLimit : x+width, offset+sOffset+21, 0xffee6060);
						Mania.instance.fontManager.medium9.drawStringWithShadow(String.format("%s%s", debu.value, debu.unit), x+4, offset+sOffset+11.5f, 0xffe0e0e0);
						if (focusedDope) {
							MiscUtils.setDoubleValue(debu, x+1, offset+sOffset, 95, mouseX, mouseY);
						}
						sOffset += 22;
					}else if(s instanceof ColorSetting) {
						ColorSetting sex = (ColorSetting) s;
						((ColorSetting) s).drawPicker(x+1, offset+sOffset+9, 1, 0xff232323);
						sOffset += 63;
					}else if(s instanceof TextSetting) {
						TextSetting onani = (TextSetting) s;
						sOffset += 26;
					}
				}
				animatedSetting[i] = AnimationUtils.animate(animatedSetting[i], settingExpanded[i] ? getSettingSize(modules.get(i).settings) : 13);
				offset += animatedSetting[i];
			}
		}
		Stencil.dispose();
		super.drawPanel(mouseX, mouseY);
	}
	
	@Override
	public void onClicked(int mouseX, int mouseY, int mouseButton) {
		// expand
		if(ClickUtils.isMouseHovering(x, y, 95, 16, mouseX, mouseY)) {
			if(mouseButton == 1) {
				expanded = !expanded;
				return;
			}
		}
		if (!expanded) return;
		// modules
		{
			int offset = y+17;
			for(int i = 0; i < modules.size(); i++) {
				if(ClickUtils.isMouseHovering(x+1, offset, 93, 12.5f, mouseX, mouseY)) {
					if(mouseButton == 0) {
						modules.get(i).toggle();
					}else {
						settingExpanded[i] = !settingExpanded[i];
					}
				}
				// setting
				int sOffset = 13;
				for(Setting s : modules.get(i).settings) {
					if(sOffset >= animatedSetting[i]) break;
					if(!(boolean) s.visibility.get()) continue;
					if(s instanceof BooleanSetting) {
						BooleanSetting busu = (BooleanSetting) s;
						if(ClickUtils.isMouseHovering(x+84, offset+sOffset, 9, 10, mouseX, mouseY)) {
							busu.value = !busu.value;
							return;
						}
						if (ClickUtils.isMouseHovering(x+1, offset+sOffset, 95, 13, mouseX, mouseY)) focusedSetting = s;
						sOffset += 13;
					}else if(s instanceof ModeSetting) {
						ModeSetting obusu = (ModeSetting) s;
						if(ClickUtils.isMouseHovering(x, offset+sOffset, 95, 13, mouseX, mouseY)) {
							obusu.cycle(mouseButton == 0);
							s.parentModule.onSetting();
							return;
						}
						if (ClickUtils.isMouseHovering(x+1, offset+sOffset, 95, 13, mouseX, mouseY)) focusedSetting = s;
						sOffset += 13;
					}else if(s instanceof DoubleSetting) {
						DoubleSetting debu = (DoubleSetting) s;
						float width = (float) MiscUtils.getPercent(debu)*90;
						Render2DUtils.drawRect(x+2.5f, offset+sOffset+10.5f, x+90, offset+sOffset+21, 0xff202020);
						Render2DUtils.drawRect(x+2.5f, offset+sOffset+10.5f, width > animatedDoubleLimit ? animatedDoubleLimit : x+width, offset+sOffset+21, 0xffee6060);
						Mania.instance.fontManager.medium9.drawStringWithShadow(String.format("%s%s", debu.value, debu.unit), x+4, offset+sOffset+11.5f, 0xffe0e0e0);
						if (ClickUtils.isMouseHovering(x+1, offset+sOffset, 95, 22, mouseX, mouseY)) {
							focusedSetting = s;
						}
						sOffset += 22;
					}else if(s instanceof ColorSetting) {
						ColorSetting sex = (ColorSetting) s;
						if (ClickUtils.isMouseHovering(x+1, offset+sOffset, 95, 63, mouseX, mouseY)) focusedSetting = s;
						sex.onClicked(mouseX, mouseY);
						sOffset += 63;
					}else if(s instanceof TextSetting) {
						TextSetting onani = (TextSetting) s;
						if (ClickUtils.isMouseHovering(x+1, offset+sOffset, 95, 26, mouseX, mouseY)) focusedSetting = s;
						sOffset += 26;
					}
				}
				offset += settingExpanded[i] ? getSettingSize(modules.get(i).settings) : 13;
			}
		}
		super.onClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected boolean isOn(int mouseX, int mouseY) {
		return ClickUtils.isMouseHovering(x, y, 95, 16, mouseX, mouseY);
	}
	
	private int getSize() {
		if(!expanded) return 16;
		int size = 15;
		for(int i = 0; i < modules.size(); i++) {
			size += animatedSetting[i];
		}
		return size;
	}
	
	private int getSettingSize(List<Setting> settings) {
		int sOffset = 13;
		for (Setting s : settings) {
			if (!(boolean) s.visibility.get()) continue;
			if (s instanceof BooleanSetting) {
				sOffset += 13;
			} else if(s instanceof ModeSetting) {
				sOffset += 13;
			} else if(s instanceof DoubleSetting) {
				sOffset += 22;
			} else if(s instanceof ColorSetting) {
				sOffset += 63;
			} else if(s instanceof TextSetting) {
				sOffset += 26;
			}
		}
		return sOffset;
	}

}
