package wtf.mania.util;

import net.minecraft.util.text.TextComponentString;
import wtf.mania.MCHook;

public class ChatUtils implements MCHook {
	
	// TODO colored prefix
	public static void printClient(String message) {
		mc.player.addChatMessage(new TextComponentString(message));
	}
	
	public static void printDebug(String message) {
		mc.player.addChatMessage(new TextComponentString(message));
	}

}
