package wtf.mania.gui.notification.toggle;

import wtf.mania.event.impl.EventRender2D;
import wtf.mania.gui.notification.AbstractNotificationManager;

public class ToggleNotificationManager extends AbstractNotificationManager<ToggleNotification> {
	
	@Override
	public void drawNotifications(EventRender2D event) {
		if (!notifications.isEmpty()) {
			notifications.get(0).drawNotification();
			if (notifications.get(0).shouldDelete()) notifications.remove(0);
		}
		for (int i = 0; i < notifications.size(); i++) {
			if (i > 0) notifications.get(i).initTime = System.currentTimeMillis();
		}
	}

}
