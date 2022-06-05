package wtf.mania.management.commmand;

import wtf.mania.MCHook;
import wtf.mania.event.EventManager;

public abstract class Command implements MCHook {
	
	public final String name, lowerName, desc;
	private final boolean eventable;
	
	public Command(String name, String desc, boolean eventable) {
		this.name = name;
		this.lowerName = name.toLowerCase();
		this.desc = desc;
		this.eventable = eventable;
	}
	
	public final void onMessage(String[] args) {
		onCalled(args);
	}
	
	public abstract void onCalled(String[] args);

}
