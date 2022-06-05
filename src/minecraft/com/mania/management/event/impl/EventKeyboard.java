package com.mania.management.event.impl;

import com.mania.management.event.Event;

public class EventKeyboard extends Event {
	
	private static final EventKeyboard instance;
	
	static {
		instance = new EventKeyboard();
	}
	
	public static void call(int key) {
		instance.keyCode = key;
		instance.call();
	}
	
	private int keyCode;
	
	private EventKeyboard() {
		
	}
	
	public int getKeyCode() {
		return this.keyCode;
	}

}
