package com.mania.management.event.impl;

import java.util.function.Function;

import com.mania.MCHook;
import com.mania.management.event.Event;

public class EventUpdate extends Event implements MCHook {

	public double x, y, z;
	public boolean sprinting, sneaking, onGround;
	private final boolean pre;
	
	public EventUpdate(boolean pre) {
		this.pre = pre;
		if (pre) {
			this.x = mc.player.posX;
			this.y = mc.player.posY;
			this.z = mc.player.posZ;
			this.sprinting = mc.player.isSprinting();
			this.sneaking = mc.player.isSneaking();
			this.onGround = mc.player.onGround;
		}
	}
	
	@Override
	public void call() {
		super.call();
	}
	
	public boolean isPre() {
		return this.pre;
	}

}
