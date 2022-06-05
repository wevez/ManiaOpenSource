package com.mania.module.impl.combat;

import org.lwjgl.input.Mouse;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.util.RandomUtil;
import com.mania.util.TimerUtil;

public class AutoClicker extends Module {
	
	private final ModeSetting clickTiming;
	private final DoubleSetting mincps, maxcps;
	private final BooleanSetting cooldown;
	
	private final TimerUtil timer;
	private double clickDelay;
	
	public AutoClicker() {
		super("AutoClicker", "Automatically clicks mouse instead of you.", ModuleCategory.Combat, true);
		this.timer = new TimerUtil();
		clickTiming = new ModeSetting("Attack Timing", this, "Pre", "Post");
		cooldown = new BooleanSetting("Cooldown", this, false);
		mincps = new DoubleSetting("Min CPS", this, () -> !cooldown.getValue(), 8, 0, 20, 1, "cps");
		maxcps = new DoubleSetting("Max CPS", this, () -> !cooldown.getValue(), 10, 0, 20, 1, "cps");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			if (clickTiming.is("Pre")) runClicker();
		} else {
			if (clickTiming.is("Post")) runClicker();
		}
	}
	
	private void runClicker() {
		if (mc.gameSettings.keyBindAttack.isKeyDown()) {
			if (timer.hasReached(clickDelay)) {
				timer.reset();
				clickDelay = 1000 / RandomUtil.nextDouble(mincps.getValue(), maxcps.getValue());
				mc.clickMouse();
			}
		}
	}

}
