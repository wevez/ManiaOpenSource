package wtf.mania.gui.notification.info;

import wtf.mania.event.impl.EventRender2D;
import wtf.mania.gui.notification.AbstractNotificationManager;

public class InfoNotificationManager extends AbstractNotificationManager<InfoNotification> {
	
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
