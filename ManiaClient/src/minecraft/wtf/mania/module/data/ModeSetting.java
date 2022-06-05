package wtf.mania.module.data;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import wtf.mania.module.Module;

public class ModeSetting extends Setting<String> {
	
	public int index;
	public final List<String> modes;
	
	public boolean expanded;
	
	public ModeSetting(String name, Module parentModule, Supplier<Boolean> visibility, String value, String[] modes) {
		super(name, parentModule, visibility, value);
		this.modes = Arrays.asList(modes);
	}
	
	public ModeSetting(String name, Module parentModule, String value, String[] modes) {
		super(name, parentModule, value);
		this.modes = Arrays.asList(modes);
	}
	
	@Override
	public String getValue() {
		return modes.get(index);
	}
	
	public List<String> getModes() {
		return modes;
	}
	
	public boolean setValue(String value) {
		for(int i = 0; i < modes.size(); i++) {
			if(modes.get(i).toLowerCase().equalsIgnoreCase(value.toLowerCase())) {
				this.value = value;
				this.index = i;
				return true;
			}
		}
		return false;
	}
	
	public void cycle(boolean positive) {
		if(positive) {
			if(index < modes.size()-1) {
				index++;
			}else {
				index = 0;
			}
		}else {
			if(index == 0)
				index = modes.size()-1;
			else
				index--;
		}
		value = getValue();
	}

	public boolean is(String mode) {
		return value.equals(mode);
	}

}
