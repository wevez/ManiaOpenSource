package wtf.mania.module.impl.render;

import java.awt.Color;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.impl.gui.ActiveMods;
import wtf.mania.util.render.ColorUtils;

public class JumpCircle extends Module {

	private final ModeSetting colorMode;
	private final ColorSetting color;
	private final DoubleSetting colorDelay;
	
	public JumpCircle() {
		super("JumpCircle", "Render circle where you jumped", ModuleCategory.Render, true);
		colorMode = new ModeSetting("Colro Mode", this, "Astolfo", new String[] { "Astolfo", "Rainbow", "Custom", "Fade" });
		color = new ColorSetting("Color", this, () -> colorMode.is("Custom") || colorMode.is("Fade"), Color.WHITE);
		colorDelay = new DoubleSetting("Delay", this, () -> (colorMode.is("Rainbow") || colorMode.is("Astolfo")), 5, 1, 10, 1, "sec");
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		
	}
	
	private int getColor() {
		switch (colorMode.value) {
		case "custom":
			return color.value.getRGB();
		case "Rainbow":
			return ColorUtils.rainbow(colorDelay.value.intValue() * 1000, 0.75f, 0.75f, 0);
		case "Astolfo":
			return ActiveMods.getAstolfoColor(0, colorDelay.value.intValue() * 1000);
		default:
			return- 1;
		}
	}

}
