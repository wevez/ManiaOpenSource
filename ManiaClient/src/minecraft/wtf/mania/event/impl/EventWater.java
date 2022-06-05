package wtf.mania.event.impl;

import wtf.mania.event.Event;

public class EventWater extends Event<EventWater> {
	
	private boolean canBePushed;

	public EventWater(boolean canBePushed) {
		this.canBePushed = canBePushed;
	}

}
