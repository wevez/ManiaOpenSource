package com.mania.management.notification;

import java.util.LinkedList;
import java.util.List;

public abstract class NotificationManager <E extends Notification> {
	
	private final List<E> notifications;
	
	public NotificationManager() {
		this.notifications = new LinkedList<>();
	}

}
