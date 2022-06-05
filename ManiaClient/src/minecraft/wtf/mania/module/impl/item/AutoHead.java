package wtf.mania.module.impl.item;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.util.PlayerUtils;
import wtf.mania.util.Timer;

public class AutoHead extends Module {
	
	private DoubleSetting health, delay;
	private Timer delayTimer;
	
	public AutoHead() {
		super("AutoHead", "Automatically use heads", ModuleCategory.Item, true);
		health = new DoubleSetting("Health", this, 7, 0.5, 10, 0.1, "Heart");
		delay = new DoubleSetting("Delay", this, 0.3, 0, 1, 0.1);
		delayTimer = new Timer();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			if (PlayerUtils.getTotalHealth(mc.player) <= health.value) {
				final int headSlot = getHead();
			}
		}
	}
	
	/*
	 * if the player do not have any heads this will return -1
	 */
	private int getHead() {
		return -1;
	}

}
