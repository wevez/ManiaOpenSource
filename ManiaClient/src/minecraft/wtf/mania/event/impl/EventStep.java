package wtf.mania.event.impl;

import wtf.mania.event.Event;

public class EventStep extends Event<EventStep> {
	
	public double stepHeight;
	public double realHeight;
    public boolean active;
    public final boolean pre;
    
    public EventStep(boolean state, double stepHeight, double realHeight) {
        this.pre = state;
        this.stepHeight = stepHeight;
        this.realHeight = realHeight;
    }

    public EventStep(boolean state, double stepHeight) {
        this.pre = state;
        this.realHeight = stepHeight;
        this.stepHeight = stepHeight;
    }

}
