package nazo.event.events;

import nazo.event.Event;
import net.minecraft.network.Packet;

public class EventGetPacket extends Event{
	
	public boolean cancelled;
	public Packet<?> packet;
	
	public EventGetPacket(Packet packet) {
		this.packet = packet;
	}

}
