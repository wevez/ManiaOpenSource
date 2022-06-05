package wtf.mania.gui.notification;

import java.util.LinkedList;
import java.util.List;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;

public abstract class AbstractNotificationManager <E extends AbstractNotification> {
	
	public final List<E> notifications;
	
	public AbstractNotificationManager() {
		notifications = new LinkedList<>();
	}
	
	public void drawNotifications(EventRender2D event) {
		if(!notifications.isEmpty()) {
			notifications.get(0).drawNotification();
			if(notifications.get(0).shouldDelete()) notifications.remove(0);
		}
	}

}
