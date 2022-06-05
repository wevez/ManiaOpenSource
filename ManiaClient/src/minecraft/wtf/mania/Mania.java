package wtf.mania;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;


import net.minecraft.client.gui.GuiScreen;
import viamcp.ViaMCP;
import wtf.mania.event.EventManager;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.gui.click.akrien.AkrienClickGui;
import wtf.mania.gui.click.flux.FluxClickGui;
import wtf.mania.gui.click.html.HTMLClickGui;
import wtf.mania.gui.click.novoline.NovolineClickGui;
import wtf.mania.gui.click.ryo.RyoClickGui;
//import wtf.mania.gui.click.sigma.JelloClickGui;
import wtf.mania.gui.click.skeet.SkeetClickGui;
import wtf.mania.gui.click.tenacity.TenacityClickGui;
import wtf.mania.gui.music.GuiMusic;
import wtf.mania.gui.notification.info.InfoNotificationManager;
import wtf.mania.gui.screen.GuiKeybindManager;
import wtf.mania.gui.screen.GuiManiaMap;
import wtf.mania.gui.screen.GuiSearch;
import wtf.mania.gui.screen.alt.Server;
import wtf.mania.gui.site.SiteViewer;
import wtf.mania.management.commmand.CommandManager;
import wtf.mania.management.file.ConfigManager;
import wtf.mania.management.file.FileFactory;
import wtf.mania.management.file.impl.AccountsFile;
import wtf.mania.management.file.impl.ModulesFile;
import wtf.mania.management.font.FontManager;
import wtf.mania.management.friend.FriendManager;
import wtf.mania.management.keybind.Keybind;
import wtf.mania.management.keybind.KeybindManager;
import wtf.mania.management.map.XaeroMinimap;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleManager;
import wtf.mania.util.MiscUtils;

public class Mania implements MCHook {
	
	public static Mania instance = new Mania();
	
	public static String name, version, user, hwid;
	
	public static Server currentSercer;
	
	public ModuleManager moduleManager;
	public FontManager fontManager;
	public KeybindManager keybindManager;
	public CommandManager commandManager;
	public FileFactory fileFactory;
	public XaeroMinimap minimap;
	public ConfigManager configManager;
	public SiteViewer siteViwer;
	public InfoNotificationManager infoNotificationManager;
	public GuiMusic music;
	public FriendManager friendManager;
	public GuiManiaMap maniamap;
	
	public GuiScreen clickGui, guiKeybindManager;
	
	public void onSetup() {
		// loader
		hwid = MiscUtils.getHashedHWID();
		name = "Mania";
		version = "2.0";
		user = "Onakin";
		Display.setTitle(name);
		friendManager = new FriendManager();
		configManager = new ConfigManager();
		commandManager = new CommandManager();
		fontManager = new FontManager();
		keybindManager = new KeybindManager();
		moduleManager = new ModuleManager();
		clickGui = new NovolineClickGui();
		moduleManager.array.forEach((m) -> m.onSetting());
		keybindManager.array.add(new Keybind("ClickGui", clickGui, Keyboard.KEY_RSHIFT));
		guiKeybindManager = new GuiKeybindManager();
		keybindManager.array.add(new Keybind("Keybind Manager", guiKeybindManager, 0));
		GuiSearch.instance = new GuiSearch();
		keybindManager.array.add(new Keybind("Spotlight", GuiSearch.instance, 0));
		currentSercer = new Server("", -1);
		keybindManager.init();
		fileFactory = new FileFactory();
		fileFactory.setupRoot("Mania");
		fileFactory.add(new ModulesFile(), new AccountsFile());
		fileFactory.load();
		minimap = new XaeroMinimap();
		siteViwer = new SiteViewer();
		keybindManager.array.add(new Keybind("Site Viewer", siteViwer, Keyboard.KEY_P));
		//playerManager = new wtf.mania.management.player.PlayerManager();
		for (Module m : moduleManager.array) {
			m.onSetting();
		}
		//minimap.load();
		music = new GuiMusic();
		keybindManager.array.add(new Keybind("Music Player", music, Keyboard.KEY_M));
		try {
			ViaMCP.getInstance().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		EventManager.register(instance);
		infoNotificationManager = new InfoNotificationManager();
		maniamap = new GuiManiaMap();
		keybindManager.array.add(new Keybind("Mania map", maniamap, Keyboard.KEY_U));
		
	}
	
	public void onShutdown() {
		fileFactory.save();
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		infoNotificationManager.drawNotifications(event);
	}

}
