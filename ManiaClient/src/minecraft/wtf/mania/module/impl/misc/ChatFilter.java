package wtf.mania.module.impl.misc;

import wtf.mania.event.impl.EventPacket;

import java.util.ArrayList;

import net.minecraft.network.play.client.CPacketChatMessage;
import wtf.mania.event.EventTarget;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class ChatFilter extends Module {
	
	public ChatFilter() {
		super("ChatFilter", "Bypasse chat filters", ModuleCategory.Player, true);
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.outGoing) {
			if (event.packet instanceof CPacketChatMessage) {
				CPacketChatMessage p = (CPacketChatMessage) event.packet;
	            String finalmsg = "";
	            ArrayList<String> splitshit = new ArrayList<>();
	            String[] msg = p.getMessage().split(" ");
	            for (int i = 0; i < msg.length; i++) {
	                char[] characters = msg[i].toCharArray();
	                for (int i2 = 0; i2 < characters.length; i2++) {
	                    splitshit.add(characters[i2] + "\u061C");
	                }
	                splitshit.add(" ");
	            }
	            for (int i = 0; i < splitshit.size(); i++) {
	                finalmsg += splitshit.get(i);
	            }
	            p.setMessage(finalmsg.replaceFirst("%", ""));
	            splitshit.clear();
			}
		}
	}

}
