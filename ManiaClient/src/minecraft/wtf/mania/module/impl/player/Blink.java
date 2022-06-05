package wtf.mania.module.impl.player;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.network.play.client.CPacketPlayer;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;

public class Blink extends Module {
	
	private static List<CPacketPlayer> packets;
	
	private static BooleanSetting packetLimit;
	private static DoubleSetting limitSize;
	
	public Blink() {
		super("Blink", "Stops your packets to blink", ModuleCategory.Player, true);
		packets = new LinkedList<>();
		settings.add(packetLimit = new BooleanSetting("Packet Limit", this, false));
		settings.add(limitSize = new DoubleSetting("Limit Size", this, 20, 1, 500, 1, "Packets"));
	}
	
	@Override
	protected void onDisable() {
		packets.forEach(p -> mc.getConnection().sendPacket(p));
		packets.clear();
		suffix = "";
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if(event.outGoing && event.packet instanceof CPacketPlayer) {
			event.cancell();
			packets.add((CPacketPlayer) event.packet);
			suffix = String.valueOf(packets.size());
			if (packetLimit.value && limitSize.value == packets.size()) {
				toggle();
				// TODO disable notidication
			}
		}
	}

}
