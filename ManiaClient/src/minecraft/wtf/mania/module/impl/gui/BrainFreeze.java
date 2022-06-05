package wtf.mania.module.impl.gui;

import java.util.LinkedList;
import java.util.List;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.gui.particle.SnowParticle;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ModeSetting;

public class BrainFreeze extends Module {
	
	public static SnowParticle particle;
	
	public BrainFreeze() {
		super("BrainFreeze", "Render snow in the ClickGUI", ModuleCategory.Gui, true);
	}
	
	@Override
	protected void onEnable() {
		particle = new SnowParticle(mc.displayWidth, mc.displayHeight);
	}
	
	public static void onResize() {
		particle = new SnowParticle(mc.displayWidth, mc.displayHeight);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		if (mc.currentScreen != null) {
			particle.drawParticles();
		}
	}
	
}
