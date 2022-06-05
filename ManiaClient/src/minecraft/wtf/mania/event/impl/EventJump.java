package wtf.mania.event.impl;

import wtf.mania.event.Event;

public class EventJump extends Event<EventJump> {
	
	public double motionY;
    public final boolean pre;

    public EventJump(boolean pre, double motionY) {
        this.pre = pre;
        this.motionY = motionY;
    }

}
