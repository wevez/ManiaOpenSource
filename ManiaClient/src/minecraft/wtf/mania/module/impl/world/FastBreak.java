package wtf.mania.module.impl.world;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;

public class FastBreak extends Module {
	
	private static DoubleSetting mp;
	private static BooleanSetting packet;
	
	public FastBreak() {
		super("FastBreak", "Break blocks faster", ModuleCategory.World, true);
		settings.add(mp = new DoubleSetting("MP", this, 0.7, 0, 1, 0.1));
		settings.add(packet = new BooleanSetting("Packet", this, true));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
	}

}
