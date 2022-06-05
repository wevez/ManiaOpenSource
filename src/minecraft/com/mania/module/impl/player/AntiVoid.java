package com.mania.module.impl.player;

import com.mania.management.event.impl.EventUpdate;
import com.mania.management.event.EventTarget;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.ModeSetting;
import com.mania.util.TimerUtil;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class AntiVoid extends Module {
	
	private final TimerUtil timer;
	
	private final ModeSetting type;
	
	private Runnable runner;
	
	public AntiVoid() {
		super("AntiVoid", "Preventsxs you from falling into void.", ModuleCategory.Player, true);
		type = new ModeSetting("Type", this, v -> {
			switch (v) {
			case "Basic":
				
				break;
			case "Matrix":
				
				break;
			}
		}, "Basic", "Matrix");
		this.timer = new TimerUtil();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!event.isPre()) {
			runner.run();
		}
	}
	
	public static boolean inVoid() {
		return false;
	}

}
