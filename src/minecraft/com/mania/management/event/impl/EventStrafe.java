package com.mania.management.event.impl;

import com.mania.management.event.Event;
import com.mania.util.RotationUtil;

public class EventStrafe extends Event {
	
	private float forward, strafe;
	public float yaw, pitch;
	
	public EventStrafe(float forward, float strafe, float yaw, float pitch) {
		this.forward = forward;
		this.strafe = strafe;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public final void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public final void setForward(float forward) {
		this.forward = forward;
	}
	
	public final void setStrafe(float strafe) {
		this.strafe = strafe;
	}

}
