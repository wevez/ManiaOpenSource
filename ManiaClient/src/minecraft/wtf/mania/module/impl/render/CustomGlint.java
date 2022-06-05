package wtf.mania.module.impl.render;

import java.awt.Color;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ColorSetting;

public class CustomGlint extends Module {
	
	public static Module instance;
	
	public static ColorSetting color;
	
	public CustomGlint() {
		super("CustomGlint", "CustomGlint", ModuleCategory.Render, false);
		settings.add(color = new ColorSetting("Color", this, Color.RED));
		instance = this;
	}

}
