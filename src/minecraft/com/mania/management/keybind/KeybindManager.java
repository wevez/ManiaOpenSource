package com.mania.management.keybind;

import java.util.ArrayList;
import java.util.List;

public class KeybindManager {
	
	private final List<Keybind> keybinds;
	
	public KeybindManager() {
		keybinds = new ArrayList<>();
	}
	
	public void register(String name, Bindable parent, int keyCode) {
		this.keybinds.add(new Keybind(name, parent, keyCode));
	}
	
	public void keydown(int keyCode) {
		keybinds.forEach(k -> k.keydown(keyCode));
	}
	
	public void setKeyCode(String found, int keyCode) {
		keybinds.forEach(k -> {
			if (k.getName().equals(found)) {
				k.setKeyCode(keyCode);
			}
		});
	}

}
