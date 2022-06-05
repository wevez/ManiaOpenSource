package wtf.mania.module.data;

import java.util.function.Supplier;

import wtf.mania.Mania;
import wtf.mania.module.Module;

public class TextSetting extends Setting<StringBuffer> {

	public TextSetting(String name, Module parentModule, Supplier<Boolean> visibility, StringBuffer value) {
		super(name, parentModule, visibility, value);
	}
	
	public TextSetting(String name, Module parentModule, StringBuffer value) {
		super(name, parentModule, value);
	}

}
