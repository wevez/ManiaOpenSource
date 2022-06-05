package com.mania.module.impl.combat;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.util.RandomUtil;
import com.mania.util.TimerUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult.Type;
public class TriggerBot extends Module {
	
	private final ModeSetting attackTiming;
	private final DoubleSetting mincps, maxcps;
	//private final DoubleSetting hitboxExpand;
	private double nextAttackDelay;
	
	private final TimerUtil attackTimer;
	
	public TriggerBot() {
		super("TriggerBot", "", ModuleCategory.Combat, true);
		attackTiming = new ModeSetting("Attack Timing", this, "Pre", "Post");
		mincps = new DoubleSetting("Min CPS", this, 8, 0, 20, 1, "cps");
		maxcps = new DoubleSetting("Max CPS", this, 10, 0, 20, 1, "cps");
		//hitboxExpand = new DoubleSetting("")
		this.attackTimer = new TimerUtil();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) { 
			if (attackTiming.is("Pre")) runUpdate();
		} else {
			if (attackTiming.is("Post")) runUpdate();
		}
	}
	
	private void runUpdate() {
		if (attackTimer.hasReached(nextAttackDelay)) {
			if (mc.objectMouseOver != null) {
				if (mc.objectMouseOver.typeOfHit == Type.ENTITY) {
					if (!mc.objectMouseOver.entityHit.isEntityAlive()) return;
					if (mc.objectMouseOver.entityHit instanceof EntityPlayer) {
						if (Teams.isTeam((EntityPlayer) mc.objectMouseOver.entityHit)) return;
						if (AntiBot.isBot((EntityPlayer) mc.objectMouseOver.entityHit)) return;
					}
					attackTimer.reset();
					mc.clickMouse();
					this.nextAttackDelay = 1000d / RandomUtil.nextDouble(mincps.getValue(), maxcps.getValue());
					return;
				}
			}
		}
	}

}
