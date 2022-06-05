package wtf.mania.event.impl;

import net.minecraft.util.math.Vec3d;
import wtf.mania.event.Event;

public class EventMove extends Event<EventMove> {
	
	public double x, y, z;

	public EventMove(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(Vec3d vec) {
		this.x = vec.xCoord;
		this.y = vec.yCoord;
		this.z = vec.zCoord;
	}
}
