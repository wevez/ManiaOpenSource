package wtf.mania.module.impl.world;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;

public class FastPlace extends Module {
	
	private static DoubleSetting delay;
	private static BooleanSetting reduceDelay;
	
	public FastPlace() {
		super("FastPlace", "Allows you to place blocks faster", ModuleCategory.World, true);
		settings.add(reduceDelay = new BooleanSetting("Reduce Delay", this, true));
		settings.add(delay = new DoubleSetting("Delay", this, null, 0, 0, 20, 1, "tick"));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			if (mc.gameSettings.keyBindUseItem.isKeyDown()) mc.rightClickMouse();
		} else {
			
		}
		
	}

}
