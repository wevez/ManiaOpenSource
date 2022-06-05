package com.mania.management.command.impl;

import com.mania.Mania;
import com.mania.management.command.Command;
import com.mania.util.ChatUtil;

public class CommandHelp extends Command {
	
	public CommandHelp() {
		super("help", "help");
	}
	
	@Override
	protected void call(String[] args) {
		Mania.getCommandManager().getCommands().forEach(c -> ChatUtil.printClient(c.getDiscription()));
	}

}
