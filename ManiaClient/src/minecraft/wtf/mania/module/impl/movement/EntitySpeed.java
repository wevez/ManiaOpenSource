package wtf.mania.module.impl.movement;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class EntitySpeed extends Module {
	
	public static Module instance;
	
	public EntitySpeed() {
		super("EntitySpeed", "Speed up your rideable entities", ModuleCategory.Movement, false);
	}

}
