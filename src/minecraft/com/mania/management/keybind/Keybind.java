package com.mania.management.keybind;

public class Keybind {
	
	private final String name;
	private int keyCode;
	private final Bindable parent;
	
	// don't let make this instance from other than key bind manager
	Keybind(String name, Bindable parent, int keyCode) {
		this.name = name;
		this.keyCode = keyCode;
		this.parent = parent;
	}
	
	public void keydown(int keyCode) {
		if (keyCode == this.keyCode) {
			this.parent.keydown();
		}
	}
	
	Bindable getParent() {
		return this.parent;
	}
	
	void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}
	
	public final String getName() {
		return this.name;
	}
	
}
