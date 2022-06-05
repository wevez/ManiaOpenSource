package wtf.mania.module.impl.world;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;

public class CakeEater extends Module {
	
	private static BooleanSetting noSwing;
	private static BooleanSetting mineplex;
	
	public CakeEater() {
		super("CakeEater", "Automatically eats cake", ModuleCategory.World, true);
		settings.add(noSwing = new BooleanSetting("NoSwing", this, true));
		settings.add(mineplex = new BooleanSetting("Mineplex", this, true));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		
	}

}
