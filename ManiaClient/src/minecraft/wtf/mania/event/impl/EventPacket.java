package wtf.mania.event.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import wtf.mania.event.Event;

public class EventPacket extends Event<EventPacket> {
	
	public final boolean outGoing;
	public Packet<?> packet;
	
	public EventPacket(Packet<?> packet, boolean outGoing) {
		this.outGoing = outGoing;
		this.packet = packet;
	}
	
	@Override
	public EventPacket call() {
		if (Minecraft.getMinecraft().player != null) return super.call();
		return this;
	}

}