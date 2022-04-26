package nazo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;

import nazo.command.CommandManager;
import nazo.module.Module;
import nazo.module.ModuleManager;
import nazo.utils.MCHook;
import nazo.utils.font.NazoFontRenderer;
import nazo.utils.font.NazoFonts;

public class Nazo implements MCHook{
	
	public static final String CLIENT_NAME = "Nazo", CLIENT_VERSION = "b1", CLIENT_AUTHOR = "NazoTeam";
	public static ModuleManager moduleManager;
	public static CommandManager commandManager;
	public static NazoFontRenderer mainFont;
	public static List<Module> modules = new ArrayList<Module>();
	public static Random random = new Random();
	public static int width, height;
	
	public static void setup() {
		mainFont = new NazoFonts().elliot18;
		moduleManager = new ModuleManager();
		commandManager = new CommandManager();
		Display.setTitle(CLIENT_NAME + " " + CLIENT_VERSION + " | " + CLIENT_AUTHOR);
		mc.logger.info(CLIENT_NAME+" has been loaded.");;
	}
	
	public static void shutdown() {
		mc.logger.info(CLIENT_NAME+" has been closed.");
	}

}
