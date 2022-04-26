package nazo.setting.settings;

import nazo.setting.Setting;

public class BooleanSetting extends Setting{
	
	public BooleanSetting(String name, boolean defaultBoolean) {
		this.name = name;
		this.toggled = defaultBoolean;
	}
	
	public boolean toggled;
	
	public void toggle() {
		this.toggled = !this.toggled;
	}

}
