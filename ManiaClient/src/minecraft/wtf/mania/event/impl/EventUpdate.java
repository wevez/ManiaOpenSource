package wtf.mania.event.impl;

import wtf.mania.event.Event;

public class EventUpdate extends Event<EventUpdate> {
	
	public double x, y, z;
	public boolean onGround, sprinting, sneaking;
	public final boolean pre;
	
	public EventUpdate(double x, double y, double z, boolean onGround, boolean sprinting,
			boolean sneaking) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.onGround = onGround;
		this.sprinting = sprinting;
		this.sneaking = sneaking;
		this.pre = true;
	};
	
	public EventUpdate() {
		this.pre = false;
	}

}
