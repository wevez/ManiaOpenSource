package wtf.mania.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import wtf.mania.MCHook;

public class PacketUtils implements MCHook {
	
	public static void sendPacketNoEvent(Packet packet) {
		mc.getConnection().sendPacket(packet);
	}
	
	public static void sendPacket(Packet packet) {
		mc.getConnection().sendPacket(packet);
	}
	
}
