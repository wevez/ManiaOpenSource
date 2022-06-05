package wtf.mania.module.impl.misc;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class PortalGui extends Module {
	
	public PortalGui() {
		super("PortalGui", "Allows GUIs while in nether portal", ModuleCategory.Misc, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		mc.player.inPortal = false;
	}

}
