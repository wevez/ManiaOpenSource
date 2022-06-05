package com.mania.management.event.impl;

import com.mania.management.event.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event {
	
	public Packet<?> packet;
	private final boolean outgoing;
	
	public EventPacket(Packet<?> packet, boolean outgoing) {
		this.packet = packet;
		this.outgoing = outgoing;
	}
	
	public boolean isOutgoing() {
		return this.outgoing;
	}

}
