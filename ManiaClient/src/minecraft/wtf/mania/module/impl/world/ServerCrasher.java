package wtf.mania.module.impl.world;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;

public class ServerCrasher extends Module {
	
	private static ModeSetting mode;
	
	public ServerCrasher() {
		super("ServerCrasher", "Crashes a server", ModuleCategory.World, true);
		settings.add(mode = new ModeSetting("Mode", this, "Flyng Enabled", new String[] {"Flyng Enabled", "Vanilla", "Book", "Infinity", "BrainFreeze"}));
	}

}
