package wtf.mania.event.impl;

import wtf.mania.event.Event;

public class EventRender2D extends Event<EventRender2D> {

	public final int width, height;
	
	public EventRender2D(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public EventRender2D call() {
		new EventPreShader(width, height).call();
		return super.call();
	}

}
