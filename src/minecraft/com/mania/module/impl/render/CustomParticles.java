package com.mania.module.impl.render;

import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.DoubleSetting;

public class CustomParticles extends Module {
	
	private static boolean enabled;
	
	//private static final DoubleSetting particleSpeed, particleAliveTime;
	
	public CustomParticles() {
		super("CUstomParticles", "Makes particles more customizable.", ModuleCategory.Render, false);
		//particleSpeed = new DoubleSetting("Particle Speed#", this, );
		//particleAliveTime = new DoubleSetting("Particle Alive Time", this, , "ticks");
	}
	
	@Override
	protected void onDisable() {
		enabled = false;
		super.onDisable();
	}
	
	@Override
	protected void onEnable() {
		enabled = true;
		super.onEnable();
	}
	
	/*public static double getParticleSpeed() {
		return enabled ? ;
	}
	
	public static int getParticleAliveTicks() {
		return enabled ? : ;
	}*/

}
