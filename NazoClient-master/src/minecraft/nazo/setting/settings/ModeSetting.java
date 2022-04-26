package nazo.setting.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nazo.setting.Setting;

public class ModeSetting extends Setting{
	
	public int index;
	public List<String> modes = new ArrayList<>();
	
	public ModeSetting(String name, String defaultMode, String...modes) {
		this.name = name;
		this.modes = Arrays.asList(modes);
		index = this.modes.indexOf(defaultMode);
	}
	
	public String getMode() {
		return modes.get(index);
	}
	
	public void setMode(String mode) {
		int index = 0;
		for(String a : modes) {
			if(a.equalsIgnoreCase(mode)) {
				this.index = index;
				return;
			}
			++index;
		}
	}
	
	public void cycle() {
		if(index < modes.size()-1) {
			index++;
		}else {
			index = 0;
		}
	}

}
