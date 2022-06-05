package wtf.mania.module.impl.world;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;

public class Timer extends Module {
	
	private static DoubleSetting timer;
	
	public Timer() {
		super("Timer", "Speeds up the world's timer", ModuleCategory.World, true);
		settings.add(timer = new DoubleSetting("Timer", this, 0.1, 0.1, 10.0, 0.1));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		mc.timer.timerSpeed = timer.value.floatValue();
	}
	
	@Override
	protected void onDisable() {
		if (mc.timer.timerSpeed == timer.value.floatValue()) {
			mc.timer.timerSpeed = 1.0f;
		}
	}

}
