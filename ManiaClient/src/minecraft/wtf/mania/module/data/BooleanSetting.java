package wtf.mania.module.data;

import java.util.function.Supplier;

import wtf.mania.module.Module;

public class BooleanSetting extends Setting<Boolean> {

	public BooleanSetting(String name, Module parentModule, Supplier<Boolean> visibility, boolean value) {
		super(name, parentModule, visibility, value);
	}
	
	public BooleanSetting(String name, Module parentModule, boolean value) {
		super(name, parentModule, value);
	}

}
