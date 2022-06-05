package wtf.mania.module.impl.player;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;

public class Sneak extends Module {
	
	public static Module instance;
	
	private static ModeSetting type;
	
	public Sneak() {
		super("Sneak", "Always sneaks", ModuleCategory.Player, true);
		settings.add(type = new ModeSetting("Type", this, "Vanilla", new String[] {"Vanilla"}));
		instance = this;
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		switch (type.value) {
		case "Vanilla":
			if (event.pre) event.sneaking = true;
			break;
			
		}
	}

}
