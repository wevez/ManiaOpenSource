package com.mania.management.event.impl;

import com.mania.management.event.Event;

public class EventRender3D extends Event {
	
	private static final EventRender3D instance;
	
	static {
		instance = new EventRender3D();
	}
	
	public static void callP() {
		instance.call();
	}
	
	private EventRender3D() {
		
	}

}
