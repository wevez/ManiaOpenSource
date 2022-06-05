package wtf.mania.module.impl.misc;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.data.TextSetting;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.Timer;

public class Spammer extends Module {
	
	private TextSetting message;
	private DoubleSetting delay;
	private ModeSetting type;
	
	private Timer timer;
	
	public Spammer() {
		super("Spammer", "Spam a message", ModuleCategory.Misc, true);
		settings.add(type = new ModeSetting("Type", this, "Custom", new String[] { "Custom", "ManiaMeme" }));
		settings.add(message = new TextSetting("Custom Message", this, new StringBuffer("Use Mania Client! It's private now!")));
		settings.add(delay = new DoubleSetting("Message delay", this, 3, 0.1, 10, 0.1));
		timer = new Timer();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!event.pre) {
			if (timer.hasReached(delay.value * 500) & org.apache.commons.lang3.RandomUtils.nextBoolean()) {
				timer.reset();
				switch (type.value) {
				case "Custom":
					mc.player.sendChatMessage(message.value.toString());
					break;
				case "ManiaMeme":
					
					break;
				}
			}
		}
	}

}
