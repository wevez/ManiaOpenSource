package wtf.mania.module.impl.world;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class NewChunks extends Module {
	
	public NewChunks() {
		super("NewChunks", "Detects new chunks on non vanilla servers", ModuleCategory.World, true);
	}

}
