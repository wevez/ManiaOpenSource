package wtf.mania.module.impl.render;

import java.awt.Color;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.ModeSetting;

public class BreadCrumbs extends Module {
	
	private static ModeSetting mode;
	private static ColorSetting sigmaColor;
	
	public BreadCrumbs() {
		super("BreadCrumbs", "Shows your taken path", ModuleCategory.Render, true);
		settings.add(mode = new ModeSetting("Mode", this, "Sigma", new String[] {"Sigma"}));
		settings.add(sigmaColor = new ColorSetting("Color", this, () -> mode.value.equals("Sigma"), Color.WHITE));
	}

}
