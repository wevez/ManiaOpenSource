package nazo.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nazo.Nazo;
import nazo.event.EventManager;
import nazo.management.notification.NotificationPublisher;
import nazo.management.notification.NotificationType;
import nazo.setting.Setting;
import nazo.utils.ChatUtils;
import nazo.utils.MCHook;

public class Module implements MCHook{

	public String name, modeName;
	public Category category;
	public boolean toggled;
	public int keyCode;
	public boolean expanded;
	public int index;
	public List<Setting> settings = new ArrayList<Setting>();

	public Module(String name, int keyCode, Category category) {
		this.modeName = "";
		this.name = name;
		this.keyCode = keyCode;
		this.category = category;
	}

	public void toggle() {
		this.toggled = !this.toggled;
		mc.thePlayer.playSound("random.click", 1f, this.toggled ? 1f : 0.8f); 
		if(this.toggled) {
			this.onEnable();
	        NotificationPublisher.queue(this.name, "Enabled " + this.name + ".", NotificationType.INFO);
			EventManager.register(this);
		}else {
			this.onDisable();
	        NotificationPublisher.queue(this.name, "Disabled " + this.name + ".", NotificationType.INFO);
			EventManager.unregister(this);
		}
	}

	public void onEnable() {}

	public void onDisable() {}

	public void addSettings(Setting...settings) {
		this.settings.addAll(Arrays.asList(settings));
	}

	public Category getCategory() {
		return this.category;
	}

	public enum Category {
		COMBAT("Combat"),
		PLAYER("Player"),
		MOVEMENT("Movement"),
		RENDER("Render"),
		WORLD("World"),
		BETA("Beta"),
		GUI("Gui"),
		OTHER("Other");

		public String name;
		public int moduleIndex;

		Category(String name){
			this.name = name;
		}
	}
}
