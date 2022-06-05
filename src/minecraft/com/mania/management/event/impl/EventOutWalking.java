package com.mania.management.event.impl;

import com.mania.management.event.Event;

public class EventOutWalking extends Event {
	
	private static final EventOutWalking instance;
	
	private EventOutWalking() {
		
	}
	
	static {
		instance = new EventOutWalking();
		
	}
	
	// returns the sneaking status
	public static boolean call(boolean sneaking) {
		instance.sneaking = sneaking;
		instance.call();
		return instance.sneaking;
	}
	
	private boolean sneaking;
	
	public final boolean isSneaking() {
		return this.sneaking;
	}
	
	public final void setSneaking(boolean flag) {
		this.sneaking = flag;
	}

}
