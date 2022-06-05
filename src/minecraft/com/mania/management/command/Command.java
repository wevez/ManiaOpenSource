package com.mania.management.command;

public abstract class Command {
	
	private final String name, discription;
	
	public Command(String name, String discription) {
		this.name = name;
		this.discription = discription;
	}
	
	protected abstract void call(String[] args);
	
	public final String getName() {
		return this.name;
	}
	
	public final String getDiscription() {
		return this.discription;
	}

}
