package nazo.event.events;

import nazo.event.Event;

public class EventUpdate extends Event{
	
	public double x, y, z;
	public float yaw, pitch;
	public boolean onGround, isPre, isPost, sprinting, sneaking;
	
	public EventUpdate(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sprinting, boolean sneaking) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
		this.sneaking = sneaking;
		this.sprinting = sprinting;
	}

}
