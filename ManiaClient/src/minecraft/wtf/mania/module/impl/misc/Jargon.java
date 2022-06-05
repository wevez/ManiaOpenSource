package wtf.mania.module.impl.misc;

import net.minecraft.network.play.client.CPacketChatMessage;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;

public class Jargon extends Module {
	
	private ModeSetting jargon;
	
	private int index;
	
	private String[] selected;
	private final String[] excuses = {
			
	}, technicals = {
			
	}, audios = {
			
	};
	
	public Jargon() {
		super("Jargon", "Sends a ramdomly contructed message in chat", ModuleCategory.Misc, true);
		jargon = new ModeSetting("Jargon", this, "Excuse", new String[] { "Excuse", "Technical", "Audio" });
	}
	
	@Override
	public void onSetting() {
		switch (jargon.value) {
		case "Excuse":
			selected = excuses;
			break;
		case "Technical":
			selected = technicals;
			break;
		case "Audio":
			selected = audios;
			break;
		}
		index = 0;
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.outGoing) {
			if (event.packet instanceof CPacketChatMessage) {
				if (index < selected.length) {
					index++;
				} else {
					index = 0;
				}
				((CPacketChatMessage) event.packet).setMessage(selected[index]);
			}
		}
	}

}
