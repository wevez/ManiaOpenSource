package com.mania.management.event.impl;

import com.mania.management.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender2D extends Event {
	
	private static final EventRender2D INSTANCE;
	
	static {
		INSTANCE = new EventRender2D();
	}
	
	public static void callP() {
		INSTANCE.call();
	}
	
	private EventRender2D() {
	}

}
