package wtf.mania.module.impl.render;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;

public class Waypoints extends Module {
	
	private static BooleanSetting unspawnPositions;
	
	public Waypoints() {
		super("Waypoints", "Renders waypoints you added in Mania maps", ModuleCategory.Render, true);
		settings.add(unspawnPositions = new BooleanSetting("Unspawn Position", this, false));
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		
	}

}
