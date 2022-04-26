package nazo.event.events;

import nazo.event.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event {
    public Packet<?> packet;
    private boolean outgoing;

    public EventPacket(Packet<?> packet, boolean outgoing) {
        this.packet = packet;
        this.outgoing = outgoing;
        this.pre = true;
    }

    public EventPacket(Packet<?> packet) {
        this.packet = packet;
        this.pre = false;
    }

    public static boolean isPre() {
        return pre;
    }

    public static boolean isPost() {
        return !pre;
    }
    
    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
    
    private static boolean pre;

    public boolean isOutgoing() {
        return outgoing;
    }

    public boolean isIncoming() {
        return !outgoing;
    }
}
