package com.mania;

import org.lwjgl.input.Keyboard;

import com.mania.gui.GuiBindable;
import com.mania.gui.clickgui.AbstractPanelClickGui;
import com.mania.gui.clickgui.mania.ManiaClickGui;
import com.mania.gui.screen.alt.AltManager;
import com.mania.gui.screen.alt.GuiAltManager;
import com.mania.management.command.CommandManager;
import com.mania.management.common.CommonSettings;
import com.mania.management.config.ConfigManager;
import com.mania.management.event.EventManager;
import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventKeyboard;
import com.mania.management.font.FontManager;
import com.mania.management.keybind.Keybind;
import com.mania.management.keybind.KeybindManager;
import com.mania.module.ModuleManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class Mania implements MCHook {
	
	public static final String name = "Mania", version = "v3", author = "wevez, nard, yajii", status, user;
	
	static {
		status = "";
		user = "";
	}
	
	private static Mania instance;
	
	// instances of manager
	private ModuleManager moduleManager;
	private FontManager fontManager;
	private KeybindManager keybindManager;
	private AltManager altManager;
	private ConfigManager configManager;
	private CommandManager commandManager;
	private CommonSettings commonSettings;
	// instances of interface
	private AbstractPanelClickGui<?> clickGui;
	private GuiScreen altManagerGui;
	private ScaledResolution sr;
	
	public static void onSetup() {
		instance = new Mania();
		EventManager.register(instance);
		instance.fontManager = new FontManager();
		instance.keybindManager = new KeybindManager();
		instance.moduleManager = new ModuleManager();
		instance.moduleManager.getModules().forEach(m -> m.getSettings().forEach(s -> s.callOnSetting()));
		instance.altManager = new AltManager();
		instance.configManager = new ConfigManager();
		instance.commandManager = new CommandManager();
		instance.commonSettings = new CommonSettings();
		// interfaces
		instance.clickGui = new ManiaClickGui();
		instance.altManagerGui = new GuiAltManager();
		// register something to the key bind manager
		instance.keybindManager.register("ClickGui", instance.clickGui, Keyboard.KEY_RSHIFT);
		instance.sr = new ScaledResolution(mc);
	}
	
	public static void onShutdown() {
		instance.configManager.onShutdown();
	}
	
	public static void onResize() {
		instance.sr = new ScaledResolution(mc);
	}
	
	public static ModuleManager getModuleManager() {
		return instance.moduleManager;
	}
	
	public static FontManager getFontManager() {
		return instance.fontManager;
	}
	
	public static KeybindManager getKeybindManager() {
		return instance.keybindManager;
	}
	
	public static AltManager getAltManager() {
		return instance.altManager;
	}
	
	public static ConfigManager getConfigManager() {
		return instance.configManager;
	}
	
	public static CommandManager getCommandManager() {
		return instance.commandManager;
	}
	
	public static int getWidth() {
		return instance.sr.getScaledWidth();
	}
	
	public static int getHeight() {
		return instance.sr.getScaledHeight();
	}
	
	@EventTarget
	public void onKeyboard(EventKeyboard event) {
		this.keybindManager.keydown(event.getKeyCode());
	}
}
