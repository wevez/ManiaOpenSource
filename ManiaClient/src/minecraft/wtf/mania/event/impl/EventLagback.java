package wtf.mania.event.impl;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;
import wtf.mania.event.Event;

public class EventLagback extends Event<EventLagback> {
	
	public EventLagback(SPacketPlayerPosLook packet, boolean pre) {
		this.pre = pre;
		this.x = packet.getX();
		this.y = packet.getY();
		this.z = packet.getZ();
		this.yaw = packet.getYaw();
		this.pitch = packet.getPitch();
		this.teleportId = packet.getTeleportId();
		this.packet = packet;
	}
	
	public int teleportId;
	public float yaw, pitch;
	public double x;
	public double y;
	public double z;
	public SPacketPlayerPosLook packet;
	public final boolean pre;

}
