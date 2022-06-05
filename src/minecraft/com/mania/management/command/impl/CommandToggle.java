package com.mania.management.command.impl;

import com.mania.Mania;
import com.mania.management.command.Command;
import com.mania.module.Module;
import com.mania.util.ChatUtil;

public class CommandToggle extends Command {
	
	public CommandToggle() {
		super("toggle", "toggle <name>");
	}
	
	@Override
	protected void call(String[] args) {
		final Module found = Mania.getModuleManager().getModule(args[1]);
		if (found == null) ChatUtil.printClient(String.format("Module <%s> was not found.", args[1]));
		else found.toggle();
	}

}
