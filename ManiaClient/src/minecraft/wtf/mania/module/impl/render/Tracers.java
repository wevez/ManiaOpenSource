package wtf.mania.module.impl.render;

import java.awt.Color;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ColorSetting;

public class Tracers extends Module {
	
	private static BooleanSetting teamColor;
	private static ColorSetting color;
	
	public Tracers() {
		super("Tracers", "Shows players", ModuleCategory.Render, true);
		settings.add(teamColor = new BooleanSetting("Team Color", this, true));
		settings.add(color = new ColorSetting("Color", this, () -> !teamColor.value, Color.RED));
	}

}
