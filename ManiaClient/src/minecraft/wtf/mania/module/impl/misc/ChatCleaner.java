package wtf.mania.module.impl.misc;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketRespawn;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class ChatCleaner extends Module {
	
	private final List<String> CHAT_LIST;
	
	public ChatCleaner() {
		super("ChatCleaner", "Cleans chat in atempt to avoid apam on anarchy server", ModuleCategory.Misc, true);
		CHAT_LIST = new LinkedList<>();
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (!event.outGoing) {
			if (event.packet instanceof SPacketRespawn) {
				CHAT_LIST.clear();
			}
			if (event.packet instanceof SPacketChat) {
				String unformatted = ((SPacketChat) event.packet).getChatComponent().getUnformattedText();
				if (CHAT_LIST.contains(unformatted)) {
					event.cancell();
					return;
				}
				if (unformatted.length() > 10) {
					for (String s : CHAT_LIST) {
						if (s.contains(unformatted.substring(5, unformatted.length() - 5))) {
							event.cancell();
							return;
						}
					}
				}
				CHAT_LIST.add(unformatted);
			}
		}
	}

}
