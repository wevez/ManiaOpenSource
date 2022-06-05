package wtf.mania.module.impl.world;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;

public class FakeLag extends Module {
	
	private static DoubleSetting lagDuration;
	private static DoubleSetting delay;
	private static BooleanSetting combat;
	private static BooleanSetting blocks;
	
	public FakeLag() {
		super("FakeLag", "Other players will see you lagging !", ModuleCategory.World, true);
		settings.add(lagDuration = new DoubleSetting("Lag duration", this, 0.3, 0.1, 2.0, 0.01));
		settings.add(delay = new DoubleSetting("Delay", this, 0.4, 0.1, 2.0, 0.01));
		settings.add(combat = new BooleanSetting("Combat", this, true));
		settings.add(blocks = new BooleanSetting("Blocks", this, true));
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		
	}

}
