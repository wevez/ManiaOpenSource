package wtf.mania.module.impl.player;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class InvMove extends Module {
	
	public static Module instance;
	
	public InvMove() {
		super("InvMove", "Move freely while opening guis", ModuleCategory.Player, false);
		instance = this;
	}

}
