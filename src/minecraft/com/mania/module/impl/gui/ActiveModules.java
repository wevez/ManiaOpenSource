package com.mania.module.impl.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.mania.Mania;
import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventRender2D;
import com.mania.management.font.TTFFontRenderer;
import com.mania.module.ModeModule;
import com.mania.module.ModeObject;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.ColorSetting;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.util.render.AnimationUtil;
import com.mania.util.render.ColorUtil;

import static com.mania.util.render.Render2DUtil.*;

public class ActiveModules extends Module {
	
	private static List<Module> sortedList;
	private static float[][] animations;
	
	private final ModeSetting mode;
	// custom options
	private final ModeSetting customFont;
	private boolean usingDefault;
	private TTFFontRenderer customFontRenderer;
	// flower options
	private BooleanSetting flowerBackground, flowerLine;
	// zero day
	private BooleanSetting zerodayBackground, zerodayLine;
	
	private Runnable listRenderer;
	private static TTFFontRenderer bold, normal, light;
	
	private static Comparator<Module> moduleComparator;
	
	public ActiveModules() {
		super("ActiveModules", "Displays toggled module list", ModuleCategory.Gui, true);
		this.bold = Mania.getFontManager().getFont("bold", 21);
		this.normal = Mania.getFontManager().getFont("normal", 24);
		this.light = Mania.getFontManager().getFont("light", 24);
		moduleComparator = Comparator.comparingDouble(m -> -bold.getWidth(String.format("%s%s", m.getSuffix() == null ? "" : m.getSuffix(), m.getName())));
		this.mode = new ModeSetting("Mode", this, v -> {
			suffix = v;
			switch (v) {
			case "Custom":
				listRenderer = () -> {
					float offset = 0f;
					for (int i = 0; i < animations.length; i++) {
						if (usingDefault) {
							
						} else {
							
						}
					}
				};
				break;
			case "Sigma":
				moduleComparator = Comparator.comparingDouble(m -> -light.getWidth(m.getName()));
				this.listRenderer = () -> {
					float offset = 0f;
					for (int i = 0; i < animations.length; i++) {
						
					}
				};
				break;
			case "SigmaClassic":
				moduleComparator = Comparator.comparingDouble(m -> -bold.getWidth(String.format("%s%s", m.getSuffix() == null ? "" : m.getSuffix().concat("|"), m.getName())));
				this.listRenderer = () -> {
					float offset = 0f;
					final List<Module> enabledList = sortedList.stream().filter(m -> m.isEnabled()).collect(Collectors.toList());
					for (int i = 0, s = enabledList.size(); i < s; i++) {
						final Module m = enabledList.get(i);
						if (m.isEnabled()) {
							final int sigmaRainbow = ColorUtil.rainbow(100000, 1f, 1f, (int) offset * 8);
							if (m.getSuffix() == null) {
								final float width = bold.getWidth(m.getName());
								rect(Mania.getWidth() - width - 1, offset, width + 1, 12, 0xb0000000);
								bold.drawString(m.getName(), Mania.getWidth() - width, offset - 1, sigmaRainbow);
								rect(Mania.getWidth() - width - 2, offset, 1, 12, sigmaRainbow);
								if (i == s - 1) rect(Mania.getWidth() - width - 2, offset + 12, width + 4, 1, sigmaRainbow);
								else {
									final Module next = enabledList.get(i + 1);
									rect(Mania.getWidth() - width - 2, offset + 12, width - bold.getWidth(next.getSuffix() == null ? next.getName() : String.format("%s%s", next.getName(), next.getSuffix())), 1, sigmaRainbow);
								}
							} else {
								final float formatedWidth = bold.getWidth(String.format("%s%s", m.getName(), m.getSuffix()));
								rect(Mania.getWidth() - formatedWidth - 2, offset, formatedWidth + 2, 12, 0xb0000000);
								bold.drawString(m.getName(), Mania.getWidth() - formatedWidth - 1, offset - 1, sigmaRainbow);
								bold.drawString(m.getSuffix(), Mania.getWidth() - bold.getWidth(m.getSuffix()), offset - 1, 0xffe0e0e0);
								rect(Mania.getWidth() - formatedWidth - 3, offset, 1, 12, sigmaRainbow);
								if (i == s - 1) rect(Mania.getWidth() - formatedWidth - 3, offset + 12, formatedWidth + 4, 1, sigmaRainbow);
								else {
									final Module next = enabledList.get(i + 1);
									rect(Mania.getWidth() - formatedWidth - 3, offset + 12, 2 + formatedWidth - bold.getWidth(next.getSuffix() == null ? next.getName() : String.format("%s%s", next.getName(), next.getSuffix())), 1, sigmaRainbow);
								}
							}
							offset += 12f;
						}
					}
				};
				break;
			case "Flux":
				moduleComparator = Comparator.comparingDouble(m -> -bold.getWidth(String.format("%s%s", m.getSuffix() == null ? "" : m.getSuffix().concat("|"), m.getName())));
				listRenderer = () -> {
					float offset = 0f;
					for (int i = 0; i < animations.length; i++) {
						final Module m = sortedList.get(i);
						if (m.isEnabled()) {
							final String formatted = String.format("%s%s", m.getName(), m.getSuffix() == null ? "" : m.getSuffix().concat("|"));
							float x = Mania.getWidth() - (bold.getWidth(formatted) + 8);
							animations[i][1] = AnimationUtil.animate(animations[i][1], 14);
							if (animations[i][1] == 14) animations[i][0] = AnimationUtil.animate(animations[i][0], x);
						} else {
							animations[i][0] = AnimationUtil.animate(animations[i][0], Mania.getWidth());
							if (animations[i][0] == Mania.getWidth()) animations[i][1] = AnimationUtil.animate(animations[i][1], 0);
						}
						if (this.animations[i][0] != Mania.getWidth()) {
							rect(animations[i][0], offset, 200, 14, 0xa0000000);
							bold.drawString(m.getName(), animations[i][0] + 2, offset, ColorUtil.rainbow(100000, 0.6f, 1f, (int) offset * 8));
							if (m.getSuffix() != null) bold.drawString(m.getSuffix(), animations[i][0] + 4 + bold.getWidth(m.getName()), offset, -1);
						}
						if (animations[i][1] != 0f) {
							rect(Mania.getWidth() - 2, offset, 2, 14, ColorUtil.rainbow(100000, 0.6f, 1f, (int) offset * 8));
							offset += animations[i][1];
						}
					}
				};
				break;
			case "Flower":
				moduleComparator = Comparator.comparingDouble(m -> -normal.getWidth(String.format("%s%s",  m.getName(), m.getSuffix() == null ? "" : m.getSuffix().concat("|"))));
				listRenderer = () -> {
					float offset = 0f;
					for (int i = 0; i < animations.length; i++) {
						final Module m = sortedList.get(i);
						if (m.isEnabled()) {
							final int screenWidth = flowerLine.getValue() ? Mania.getWidth() - 2 : Mania.getWidth();
							if (m.getSuffix() == null) {
								final float width = normal.getWidth(m.getName());
								if (flowerBackground.getValue()) rect(screenWidth - width - 4, offset, width + 4, 14, 0xd0000000);
								normal.drawStringShadow(m.getName(), screenWidth - width - 2, offset - 1, 0xff00ff00);
							} else {
								final float formatWidth = normal.getWidth(String.format("%s%s", m.getName(), m.getSuffix()));
								if (flowerBackground.getValue()) rect(screenWidth - 6 - formatWidth, offset, formatWidth + 6, 14, 0xd0000000);
								normal.drawStringShadow(m.getName(), screenWidth - formatWidth - 4, offset - 1, 0xff00ff00);
								normal.drawStringShadow(m.getSuffix(), screenWidth - normal.getWidth(m.getSuffix()) - 2, offset - 1, -1);
							}
							offset += 14;
						}
						if (flowerLine.getValue()) rect(Mania.getWidth() - 2, 0, 2, offset, 0xff00ff00);
					}
				};
				break;
			case "Mania":
				moduleComparator = Comparator.comparingDouble(m -> -bold.getWidth(String.format("%s%s", m.getSuffix() == null ? "" : m.getSuffix().concat("|"), m.getName())));
				this.listRenderer = () -> {
					
				};
				break;
			case "Zeroday":
				moduleComparator = Comparator.comparingDouble(m -> -bold.getWidth(String.format("%s%s", m.getSuffix() == null ? "" : m.getSuffix().concat("|"), m.getName())));
				this.listRenderer = () -> {
					
				};
				break;
			}
		}, "Custom", "Flower", "Flux", "Sigma", "SigmaClassic", "Mania");
		this.customFont = new ModeSetting("Font", this, () -> this.mode.is("Custom"), v -> {
			if (v.equals("Minecraft")) {
				usingDefault = true;
				customFontRenderer = null;
			} else {
				usingDefault = false;
				switch (v) {
				case "Bold": break;
				}
			}
			this.listRenderer = () -> {
				
			};
		}, "Minecraft", "");
		// flower
		this.flowerBackground = new BooleanSetting("Background", this, () -> mode.is("Flower"), false);
		this.flowerLine = new BooleanSetting("Line", this, () -> mode.is("Flower"), false);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		listRenderer.run();
	}
	
	public static void onSetting() {
		sortedList = new ArrayList<>(Mania.getModuleManager().getModules());
		animations = new float[sortedList.size()][2];
		sortedList.sort(moduleComparator);
	}

}
