package wtf.mania.module.data;

import java.util.function.Supplier;

import wtf.mania.module.Module;

public abstract class Setting <T> {
	
	public final Module parentModule;
	public final String name;
	public T value;
	public final Supplier<Boolean> visibility;
	public boolean focused;
	
	public float case0;
	
	private static Supplier<Boolean> fake = () -> true;
	
	public Setting(String name, Module parentModule, Supplier<Boolean> visibility, T value) {
		this.parentModule = parentModule;
		this.name = name;
		this.visibility = visibility;
		this.value = value;
		parentModule.settings.add(this);
	}
	
	public Setting(String name, Module parentModule, T value) {
		this.parentModule = parentModule;
		this.name = name;
		this.visibility = fake;
		this.value = value;
		parentModule.settings.add(this);
	}
	
	public T getValue() {
		return this.value;
	}

}
