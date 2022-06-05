package com.mania.management.event.impl;

import com.mania.management.event.Event;

public class EventRenderNametag extends Event {
	
	private static final EventRenderNametag instance;
	
	static {
		instance = new EventRenderNametag();
	}
	
	public static void callP() {
		instance.call();
	}

}
