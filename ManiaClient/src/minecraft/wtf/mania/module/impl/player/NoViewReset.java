package wtf.mania.module.impl.player;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;

public class NoViewReset extends Module {
	
	private static ModeSetting animation;
	
	public NoViewReset() {
		super("NoViewReset", "Prevents the server from resetting your client yaw/pitch", ModuleCategory.Player, true);
	}

}
