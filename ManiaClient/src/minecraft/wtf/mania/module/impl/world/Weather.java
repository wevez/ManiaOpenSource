package wtf.mania.module.impl.world;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;

public class Weather extends Module {
	
	private static BooleanSetting customTime;
	private static DoubleSetting time;
	private static BooleanSetting disableRain;
	
	public Weather() {
		super("Weather", "Removes rain and changes the world's time", ModuleCategory.World, true);
		settings.add(customTime = new BooleanSetting("Custom time", this, true));
		settings.add(time = new DoubleSetting("Time", this, 12000.0, 0.0, 24000.0, 1));
		settings.add(disableRain = new BooleanSetting("Disable rain", this, true));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			if (customTime.value) mc.world.setTotalWorldTime(time.value.longValue());
		}
	}

}
