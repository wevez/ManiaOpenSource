package wtf.mania.management.keybind;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import wtf.mania.MCHook;
import wtf.mania.management.Manager;
import wtf.mania.module.Module;

public class KeybindManager extends Manager<Keybind> implements MCHook {
	
	public List<Keybind> screens, modules;

	public KeybindManager() {
		super(new LinkedList<>());
		screens = new LinkedList<>();
		modules = new LinkedList<>();
	}
	
	public void init() {
		for(Keybind k : array) {
			if(k.object instanceof GuiScreen) {
				screens.add(k);
			}else if(k.object instanceof Module) {
				modules.add(k);
			}
		}
	}
	
	public int getKeyByObject(Object object) {
		for(Keybind k : array) {
			if(k.object == object) return k.keyInt;
		}
		return -1;
	}
	
	public void setKeyByObject(Object object, int keyInt) {
		for(Keybind k : array) {
			if(k.object == object) {
				k.keyInt = keyInt;
			}
		}
	}
	
	public void onKeydown(int key) {
		for(Keybind k : array) {
			if(k.keyInt == key) {
				if(k.object instanceof Module) ((Module) k.object).toggle();
				else mc.displayGuiScreen((GuiScreen) k.object);
			}
		}
	}
	
	public List<Keybind> getKeybindsByKey(int keyCode) {
		List<Keybind> keybinds = new LinkedList<>();
		for(Keybind k : array) {
			if(k.keyInt == keyCode) {
				keybinds.add(k);
			}
		}
		return keybinds;
	}

}
