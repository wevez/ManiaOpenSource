package com.mania.management.command;

import java.util.ArrayList;
import java.util.List;

import com.mania.management.command.impl.*;
import com.mania.util.ChatUtil;

public class CommandManager {
	
	private final List<Command> commands;
	
	public CommandManager() {
		commands = new ArrayList<>();
		// initialize commands
		commands.add(new CommandBind());
		commands.add(new CommandHelp());
		commands.add(new CommandToggle());
		commands.add(new CommandToggle2());
	}
	
	public void onChat(String message) {
		final String[] formatted = message.split(" ");
		if (formatted.length == 1) return;
		final String commandName = formatted[0].substring(1);
		for (Command c : commands) {
			if (commandName.equalsIgnoreCase(c.getName())) {
				c.call(formatted);
				return;
			}
		}
		ChatUtil.printClient(String.format("Command %s was not found!", commandName));
	}
	
	public final List<Command> getCommands(){
		return this.commands;
	}

}
